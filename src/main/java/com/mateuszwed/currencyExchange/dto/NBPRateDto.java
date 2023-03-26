package com.mateuszwed.currencyExchange.dto;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
