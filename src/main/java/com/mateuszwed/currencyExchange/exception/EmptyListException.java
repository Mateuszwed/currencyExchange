package com.mateuszwed.currencyExchange.exception;

import java.util.NoSuchElementException;

public class EmptyListException extends NoSuchElementException {
    public EmptyListException(String message) {
        super(message);
    }
}
