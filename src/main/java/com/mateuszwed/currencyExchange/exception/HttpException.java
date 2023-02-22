package com.mateuszwed.currencyExchange.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException {
    public HttpException(HttpStatus httpStatus){
        super(httpStatus);
    }
}
