package com.mateuszwed.currencyExchange.exception;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log
@ControllerAdvice
public class HttpExceptionHandler {
    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<String> handleHttpException(HttpException e) {
        log.warning(e.getMessage());
        return ResponseEntity.badRequest().body("Problem with call to NPB API");
    }
}
