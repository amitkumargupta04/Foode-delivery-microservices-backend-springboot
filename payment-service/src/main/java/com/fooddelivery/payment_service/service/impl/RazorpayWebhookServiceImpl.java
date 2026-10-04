package com.fooddelivery.payment_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.payment_service.client.OrderClient;
import com.fooddelivery.payment_service.entity.Payment;
import com.fooddelivery.payment_service.enums.PaymentStatus;
import com.fooddelivery.payment_service.exception.BadRequestException;
import com.fooddelivery.payment_service.exception.ResourceNotFoundException;
import com.fooddelivery.payment_service.repository.PaymentRepository;
import com.fooddelivery.payment_service.service.RazorpayWebhookService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RazorpayWebhookServiceImpl implements RazorpayWebhookService {

    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Value("${razorpay.webhook-secret}")
    private String webhookSecret;

    @Override
    @Transactional
    public void handleWebhook(String payload, String signature) {
        try {
            Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    webhookSecret
            );
            JsonNode root = objectMapper.readTree(payload);
            String event = root.get("event").asText();
            if ("payment.captured".equals(event)) {
                JsonNode paymentEntity = root.get("payload").get("payment").get("entity");
                String razorpayPaymentId = paymentEntity.get("id").asText();
                String razorpayOrderId = paymentEntity.get("order_id").asText();
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(
                        () -> new ResourceNotFoundException("Payment not found for Razorpay order: " + razorpayOrderId)
                );
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setStatus(PaymentStatus.PAID);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                orderClient.updatePaymentStatus(payment.getOrderId(), PaymentStatus.PAID);
            }
        } catch (Exception e) {
            throw new BadRequestException("Failed to process Razorpay webhook");
        }
    }
}
