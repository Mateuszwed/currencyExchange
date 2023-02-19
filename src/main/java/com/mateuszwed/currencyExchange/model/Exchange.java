package com.mateuszwed.currencyExchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {

    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;



}
