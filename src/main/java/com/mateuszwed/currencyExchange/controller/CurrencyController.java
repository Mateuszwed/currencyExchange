package com.mateuszwed.currencyExchange.controller;

import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.model.Exchange;
import com.mateuszwed.currencyExchange.service.CurrencyService;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Log
@RequestMapping("/exchanges")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyController {
    CurrencyService currencyService;
    @ApiOperation("Method convert currency exchange")
    @PostMapping
    public ResponseEntity<ExchangeDto> currencyExchange(@RequestBody Exchange exchange) {
        ExchangeDto exchangeDto = new ExchangeDto(exchange.getAmount(),
                exchange.getFromCurrency(),
                exchange.getToCurrency(),
                exchange.getAmount().multiply(new BigDecimal(2)));
        log.info("Create exchange: "
                + exchangeDto.getAmount() + " "
                + exchangeDto.getFromCurrency() + " "
                + exchangeDto.getToCurrency() + " "
                + exchangeDto.getConvertedAmount());
        return ResponseEntity.ok(exchangeDto);
    }

    @PostMapping("/2")
    public ExchangeDto currencyExchange2(@RequestBody Exchange exchange) {
        return currencyService.convertCurrency(exchange);
    }
}
