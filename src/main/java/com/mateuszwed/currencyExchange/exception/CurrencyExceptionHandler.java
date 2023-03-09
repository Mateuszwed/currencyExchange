package com.mateuszwed.currencyExchange.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CurrencyExceptionHandler {
    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<String> handleHttpException(HttpException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("Problem with call to NPB API");
    }

    @ExceptionHandler(value = EmptyListException.class)
    public ResponseEntity<String> handleEmptyListException(EmptyListException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("List with Data is empty");
    }

    @ExceptionHandler(value = NotFoundRatesException.class)
    public ResponseEntity<String> handleNullResponseException(NotFoundRatesException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("Response is null");
    }

    @ExceptionHandler(value = NoCurrencyException.class)
    public ResponseEntity<String> handleNoCurrencyException(NoCurrencyException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("This currency does not exist");
    }
}
