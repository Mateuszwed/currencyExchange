package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HttpException extends RuntimeException {
    public HttpException(String message) {
        super(message);
    }
}
