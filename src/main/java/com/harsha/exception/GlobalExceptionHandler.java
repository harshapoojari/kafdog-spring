package com.harsha.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleKafkaError(ResponseStatusException e) {
        // Return a clear message to the frontend
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
}