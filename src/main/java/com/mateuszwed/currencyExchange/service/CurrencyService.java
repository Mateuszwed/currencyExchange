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
    String NbpTableA;
    @NonFinal
    @Value("${nbp.api.url.table.b}")
    String NbpTableB;
    //Servis zaiplemenotwany tylko w celu sprawdzenia czy restTemplate zwraca całą listę walut.
    public List<NBPRateDto> getCurrency() {
        List<NBPRateDto> nbpRateDtoList = nbpApiClient.getResponseFromNBPApi(NbpTableA);
        nbpRateDtoList.addAll(nbpApiClient.getResponseFromNBPApi(NbpTableB));
        return nbpRateDtoList;
    }
}
