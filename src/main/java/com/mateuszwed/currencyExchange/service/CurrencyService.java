package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.dto.ExchangeMapper;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.NoCurrencyException;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyService {
    NBPApiClient nbpApiClient;
    ExchangeMapper exchangeMapper;
    @NonFinal
    @Value("${nbp.api.url.table.a}")
    String nbpTableA;
    @NonFinal
    @Value("${nbp.api.url.table.b}")
    String nbpTableB;

    //@Transactional
    public ExchangeRateDto convertCurrency(ExchangeDto exchange) {
        var nbpRateList = getCurrencyFromApi();
        var fromCurrency = exchange.getFromCurrency().toUpperCase();
        var toCurrency = exchange.getToCurrency().toUpperCase();
        var amount = exchange.getAmount();
        var convertedAmount = calculateCurrencyAmount(fromCurrency, toCurrency, amount, nbpRateList);
        ExchangeEntity exchangeEntity = exchangeMapper.exchangeToExchangeEntity(exchange, convertedAmount);
        //return exchangeMapper.exchangeEntityToExchangeDto(saveConvertedCurrencyToDataBase(exchangeEntity));
        return null;
    }

    private List<NBPRateDto> getCurrencyFromApi() {
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateListA = nbpApiClient.getResponseFromNBPApi(nbpTableA);
        var nbpRateListB = nbpApiClient.getResponseFromNBPApi(nbpTableB);
        nbpRateList.addAll(nbpRateListA);
        nbpRateList.addAll(nbpRateListB);
        return nbpRateList;
    }

    private BigDecimal calculateCurrencyAmount(String fromCurrency, String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        var pln = "PLN";
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }
        if (fromCurrency.equals(pln)) {
            return convertFromPln(toCurrency, amount, nbpRateList);
        }
        if (toCurrency.equals(pln)) {
            return convertToPln(fromCurrency, amount, nbpRateList);
        }
        var fromAmount = convertFromPln(toCurrency, amount, nbpRateList);
        return convertToPln(fromCurrency, fromAmount, nbpRateList);
    }

    private BigDecimal convertFromPln(String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        var averageExchangeRate = nbpRateList.stream()
            .filter(rate -> rate.getCode().equals(toCurrency))
            .map(NBPRateDto::getMid).findFirst()
            .orElseThrow(() -> new NoCurrencyException("Currency rate not found " + toCurrency));
        return averageExchangeRate.divide(amount, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal convertToPln(String fromCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        var averageExchangeRate = nbpRateList.stream()
            .filter(rate -> rate.getCode().equals(fromCurrency))
            .map(NBPRateDto::getMid)
            .findFirst()
            .orElseThrow(() -> new NoCurrencyException("Currency rate not found " + fromCurrency));
        return averageExchangeRate.multiply(amount);
    }
   /*
    private ExchangeEntity saveConvertedCurrencyToDataBase(ExchangeEntity exchangeEntity){
        return exchangeRepository.save(exchangeEntity);
    }
    */
}