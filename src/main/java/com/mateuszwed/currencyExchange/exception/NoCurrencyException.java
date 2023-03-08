package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoCurrencyException extends IllegalArgumentException {
    public NoCurrencyException(String message) {
        super(message);
    }
}
