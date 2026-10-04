package com.fooddelivery.payment_service.controller;

import com.fooddelivery.payment_service.service.RazorpayWebhookService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/payments/webhook")
@RequiredArgsConstructor
@Hidden
public class RazorpayWebhookController {

    private final RazorpayWebhookService webhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {
        webhookService.handleWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }
}
