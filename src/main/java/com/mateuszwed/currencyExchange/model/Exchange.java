package com.mateuszwed.currencyExchange.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exchange {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
}