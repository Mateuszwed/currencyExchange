package com.mateuszwed.currencyExchange.model;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class Exchange {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
}
