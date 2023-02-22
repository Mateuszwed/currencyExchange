package com.mateuszwed.currencyExchange.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class NBPRateDto {
    private String currency;
    private String code;
    private BigDecimal mid;
}
