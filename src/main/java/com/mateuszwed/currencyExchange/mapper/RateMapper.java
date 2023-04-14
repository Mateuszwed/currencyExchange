package com.mateuszwed.currencyExchange.mapper;

import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.model.RateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateMapper {

    @Mapping(target = "id", ignore = true)
    List<RateEntity> toEntity(List<NBPRateDto> nbpRateDto);

    List<NBPRateDto> toDto(List<RateEntity> rateEntity);
}
