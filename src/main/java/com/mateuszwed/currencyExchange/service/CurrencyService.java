package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
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
import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CurrencyService {
    NBPApiClient nbpApiClient;
    @NonFinal
    @Value("${nbp.api.url.table.a}")
    String nbpTableA;
    @NonFinal
    @Value("${nbp.api.url.table.b}")
    String nbpTableB;
    String pln = "PLN";

    public ExchangeDto convertCurrency(Exchange exchange) {
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateListA = nbpApiClient.getResponseFromNBPApi(nbpTableA);
        var nbpRateListB = nbpApiClient.getResponseFromNBPApi(nbpTableB);
        nbpRateList.addAll(nbpRateListA);
        nbpRateList.addAll(nbpRateListB);
        var fromCurrency = exchange.getFromCurrency().toUpperCase(Locale.ROOT);
        var toCurrency = exchange.getToCurrency().toUpperCase(Locale.ROOT);
        var amount = exchange.getAmount();
        var convertedAmount = convert(fromCurrency, toCurrency, amount, nbpRateList);

        ExchangeDto exchangeDto = ExchangeDto.builder()
                .amount(amount)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .convertedAmount(convertedAmount)
                .build();

        // Zapis do bazy danych

        return exchangeDto;
    }

    BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        if( fromCurrency.equals(toCurrency) ) {
            return amount;
        }
        if( fromCurrency.equals(pln) ) {
            return convertFromPln(toCurrency, amount, nbpRateList);
        }
        if( toCurrency.equals(pln) ) {
            return convertToPln(fromCurrency, amount, nbpRateList);
        }
        var fromAmount = convertFromPln(toCurrency, amount, nbpRateList);
        return convertToPln(fromCurrency, fromAmount, nbpRateList);
    }

    BigDecimal convertFromPln(String toCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        return nbpRateList.stream()
                .filter(rate -> rate.getCode().equals(toCurrency))
                .findFirst()
                .map(rate -> amount.divide(rate.getMid(), 2, RoundingMode.HALF_UP))
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono kursu dla waluty " + toCurrency));
    }

    BigDecimal convertToPln(String fromCurrency, BigDecimal amount, List<NBPRateDto> nbpRateList) {
        return nbpRateList.stream().filter(rate -> rate.getCode().equals(fromCurrency))
                .findFirst()
                .map(rate -> amount.multiply(rate.getMid()))
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono kursu dla waluty " + fromCurrency));
    }
}