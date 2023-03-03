package com.mateuszwed.currencyExchange.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeDto {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
    BigDecimal convertedAmount;
}
