package com.mateuszwed.currencyExchange.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NBPRateDto {
    String currency;
    String code;
    BigDecimal mid;
}
