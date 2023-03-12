package com.mateuszwed.currencyExchange.dto;

import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "convertedAmount", target = "convertedAmount")
    ExchangeEntity exchangeToExchangeEntity(ExchangeDto exchange, BigDecimal convertedAmount);

    ExchangeRateDto exchangeEntityToExchangeDto(ExchangeEntity exchangeEntity);
}
