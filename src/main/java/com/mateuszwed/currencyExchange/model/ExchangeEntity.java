package com.mateuszwed.currencyExchange.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ExchangeEntity {
    Long id;
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
    BigDecimal convertedAmount;
}
