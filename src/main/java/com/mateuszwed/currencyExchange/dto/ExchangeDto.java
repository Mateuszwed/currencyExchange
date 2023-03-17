package com.mateuszwed.currencyExchange.dto;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeDto {
    BigDecimal amount;
    String fromCurrency;
    String toCurrency;
}