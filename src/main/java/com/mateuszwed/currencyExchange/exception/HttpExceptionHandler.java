package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpExceptionHandler {

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<String> handleHttpException(HttpException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
