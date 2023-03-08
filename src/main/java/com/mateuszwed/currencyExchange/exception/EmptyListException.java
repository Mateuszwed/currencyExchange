package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyListException extends NoSuchElementException {
    public EmptyListException(String message) {
        super(message);
    }
}
