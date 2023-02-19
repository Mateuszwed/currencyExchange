package com.mateuszwed.currencyExchange.controller;

import com.mateuszwed.currencyExchange.model.Exchange;
import com.mateuszwed.currencyExchange.model.ExchangeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/exchanges")
public class currencyController {

    private static final Logger logger = LoggerFactory.getLogger(currencyController.class);

    @PostMapping
    public ResponseEntity<ExchangeDto> currencyExchange(@RequestBody Exchange exchange){
            ExchangeDto exchangeDto = new ExchangeDto(exchange.getAmount(), exchange.getFromCurrency(), exchange.getToCurrency(), new BigDecimal(200));
            logger.info("Create exchange: " + exchangeDto.getAmount() + " " + exchangeDto.getFromCurrency() + " " + exchangeDto.getToCurrency() + " " + exchangeDto.getConvertedAmount());
            return new ResponseEntity<>(exchangeDto, HttpStatus.CREATED);
    }
}
