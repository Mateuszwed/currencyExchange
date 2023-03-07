package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeMapper;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.NoCurrencyException;
import com.mateuszwed.currencyExchange.model.Exchange;
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

    public ExchangeDto convertCurrency(Exchange exchange) {
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateListA = nbpApiClient.getResponseFromNBPApi(nbpTableA);
        var nbpRateListB = nbpApiClient.getResponseFromNBPApi(nbpTableB);
        nbpRateList.addAll(nbpRateListA);
        nbpRateList.addAll(nbpRateListB);
        var fromCurrency = exchange.getFromCurrency().toUpperCase();
        var toCurrency = exchange.getToCurrency().toUpperCase();
        var amount = exchange.getAmount();
        var convertedAmount = calculateCurrencyAmount(fromCurrency, toCurrency, amount, nbpRateList);
        var exchangeDto = exchangeMapper.exchangeToExchangeDto(exchange, convertedAmount);
        // Zapis do bazy danych
        // saveConvertedCurrencyToDataBase(exchangeDto);
        return exchangeDto;
    }

    private BigDecimal calculateCurrencyAmount(String fromCurrency, String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        var pln = "PLN";
        // jeśli waluta Z i NA są takie same
        if( fromCurrency.equals(toCurrency) ) {
            return amount;
        }
        //jeśli waluta Z jest równa PLN
        if( fromCurrency.equals(pln) ) {
            return convertFromPln(toCurrency, amount, nbpRateList);
        }
        //jeśli waluta NA jest równa PLN
        if( toCurrency.equals(pln) ) {
            return convertToPln(fromCurrency, amount, nbpRateList);
        }
        //jeśli obie waluty są różne od PLN
        //convertFromPLN przelicza mi z dowolnej waluty na pln np. 50 eur na 200 pln
        //convertToPLN przelicza mi np. 200 pln na dolary
        var fromAmount = convertFromPln(toCurrency, amount, nbpRateList);
        return convertToPln(fromCurrency, fromAmount, nbpRateList);
    }

    private BigDecimal convertFromPln(String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        return nbpRateList.stream()
                .filter(rate -> rate.getCode().equals(toCurrency))
                .findFirst()
                .map(rate -> amount.divide(rate.getMid(), 2, RoundingMode.HALF_UP))
                .orElseThrow(() -> new NoCurrencyException("Nie znaleziono kursu dla waluty " + toCurrency));
    }

    private BigDecimal convertToPln(String fromCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        return nbpRateList.stream()
                .filter(rate -> rate.getCode().equals(fromCurrency))
                .findFirst()
                .map(rate -> amount.multiply(rate.getMid()))
                .orElseThrow(() -> new NoCurrencyException("Nie znaleziono kursu dla waluty " + fromCurrency));
    }
    /*
    private void saveConvertedCurrencyToDataBase(ExchangeDto exchangeDto){
        ExchangeEntity exchangeEntity = exchangeDtoToExchangeEntity(exchangeDto);
        try{
        exchangeRepository.save(exchangeEntity);
        }catch(ConstraintViolationException e)
        wyjatek
    }
    */
}