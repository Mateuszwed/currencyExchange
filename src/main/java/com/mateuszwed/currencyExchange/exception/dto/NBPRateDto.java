package com.mateuszwed.currencyExchange.exception.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NBPRateDto {
    String currency;
    String code;
    BigDecimal mid;
}
