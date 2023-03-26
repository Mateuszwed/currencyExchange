package com.mateuszwed.currencyExchange.dto;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDto {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
    BigDecimal convertedAmount;
}
