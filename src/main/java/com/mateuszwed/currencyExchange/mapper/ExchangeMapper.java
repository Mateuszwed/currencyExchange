package com.mateuszwed.currencyExchange.mapper;

import java.math.BigDecimal;

import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "convertedAmount", target = "convertedAmount")
    ExchangeEntity toEntity(ExchangeDto exchange, BigDecimal convertedAmount);

    ExchangeRateDto toDto(ExchangeEntity exchangeEntity);
}
