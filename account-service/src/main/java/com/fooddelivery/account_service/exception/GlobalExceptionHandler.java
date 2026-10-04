package com.fooddelivery.account_service.exception;

import com.fooddelivery.account_service.dto.response.StandardResponse;
import org.apache.hc.client5.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        StandardResponse<String> error = new StandardResponse<>(
                false, ex.getMessage(), HttpStatus.NOT_FOUND.value(), null, null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<StandardResponse<String>> handelInvalidCredentials(InvalidCredentialsException ex){
        StandardResponse<String> error = new StandardResponse<>(
                false, ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), null, null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StandardResponse<String>> handelDuplicate(DuplicateResourceException ex){
        StandardResponse<String> error = new StandardResponse<>(
                false, ex.getMessage(), HttpStatus.CONFLICT.value(), null, null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardResponse<String>> handelBadRequest(BadRequestException ex){
        StandardResponse<String> error = new StandardResponse<>(
                false, ex.getMessage(), HttpStatus.BAD_REQUEST.value(), null, null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
