package com.mateuszwed.currencyExchange.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
    BigDecimal convertedAmount;
}
