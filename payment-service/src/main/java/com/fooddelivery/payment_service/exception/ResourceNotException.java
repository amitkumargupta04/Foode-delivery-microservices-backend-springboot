package com.fooddelivery.payment_service.exception;

public class ResourceNotException extends RuntimeException {
    public ResourceNotException(String message) {
        super(message);
    }
}
