package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyService {
    NBPApiClient nbpApiClient;
    @NonFinal
    @Value("${nbp.api.url.table.a}")
    private String NBP_TABLE_A;
    @NonFinal
    @Value("${nbp.api.url.table.b}")
    private String NBP_TABLE_B;
    //Servis zaiplemenotwany tylko w celu sprawdzenia czy restTemplate zwraca całą listę walut.
    public List<NBPRateDto> getCurrency() {
        List<NBPRateDto> nbpRateDtoList = nbpApiClient.getResponseFromNBPApi(NBP_TABLE_A);
        nbpRateDtoList.addAll(nbpApiClient.getResponseFromNBPApi(NBP_TABLE_B));
        return nbpRateDtoList;
    }
}