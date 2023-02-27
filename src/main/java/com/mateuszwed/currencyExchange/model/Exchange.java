package com.mateuszwed.currencyExchange.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exchange {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
}
