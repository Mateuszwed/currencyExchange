package com.mateuszwed.currencyExchange.dto;

import com.mateuszwed.currencyExchange.model.Exchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    @Mapping(source = "convertedAmount", target = "convertedAmount")
    ExchangeDto exchangeToExchangeDto(Exchange exchange, BigDecimal convertedAmount);
}
