package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HttpException extends HttpStatusCodeException {
    public HttpException(HttpStatus status, String message) {
        super(status, message);
    }
}
