package com.fooddelivery.payment_service.service;

public interface RazorpayWebhookService {
    void handleWebhook(String payload, String signature);
}
