package com.mateuszwed.currencyExchange.model;
import lombok.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {

    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal convertedAmount;
}
