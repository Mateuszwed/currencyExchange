package com.mateuszwed.currencyExchange.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NBPRateDto {
    String currency;
    String code;
    BigDecimal mid;
}
