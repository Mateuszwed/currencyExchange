package com.mateuszwed.currencyExchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RatesCannotSaveToDataBaseException extends RestClientException {
    public RatesCannotSaveToDataBaseException(String message) {
        super(message);
    }
}
