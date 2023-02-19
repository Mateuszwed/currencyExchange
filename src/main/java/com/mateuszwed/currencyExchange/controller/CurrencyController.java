package com.mateuszwed.currencyExchange.controller;
import com.mateuszwed.currencyExchange.model.Exchange;
import com.mateuszwed.currencyExchange.model.ExchangeDto;
import io.swagger.annotations.ApiOperation;
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
public class CurrencyController {
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
}
