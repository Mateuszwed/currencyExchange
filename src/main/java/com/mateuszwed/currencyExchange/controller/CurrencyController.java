package com.mateuszwed.currencyExchange.controller;

import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
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

@RestController
@Log
@RequestMapping("/exchanges")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyController {
    CurrencyService currencyService;
    @ApiOperation("Method convert currency exchange")
    @PostMapping
    public ResponseEntity<ExchangeRateDto> currencyExchange(@RequestBody ExchangeDto exchangeDto) {
        return ResponseEntity.ok(currencyService.convertCurrency(exchangeDto));
    }
}
