package com.mateuszwed.currencyExchange.controller;

import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import com.mateuszwed.currencyExchange.service.CurrencyService;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
