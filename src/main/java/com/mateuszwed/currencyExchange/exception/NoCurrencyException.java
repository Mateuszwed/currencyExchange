package com.mateuszwed.currencyExchange.exception;

public class NoCurrencyException extends IllegalArgumentException {
    public NoCurrencyException(String message) {
        super(message);
    }
}
