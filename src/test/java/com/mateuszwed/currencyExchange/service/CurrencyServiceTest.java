package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeMapper;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.NoCurrencyException;
import com.mateuszwed.currencyExchange.model.Exchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    NBPApiClient nbpApiClient;
    @Mock
    ExchangeMapper exchangeMapper;
    @InjectMocks
    CurrencyService currencyService;

    @Test
    void convertCurrencyWhenFromAndToAreDifferentShouldBeReturnExchangeDtoWithConvertedAmount() {
        //given
        var exchange = Exchange.builder().fromCurrency("USD").toCurrency("EUR").amount(BigDecimal.valueOf(100)).build();
        var exchangeDtoSimple = new ExchangeDto(BigDecimal.valueOf(100), "USD", "EUR", BigDecimal.valueOf(88.880));
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeDto(any(Exchange.class), any(BigDecimal.class))).thenReturn(exchangeDtoSimple);

        //when
        var result = currencyService.convertCurrency(exchange);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("USD");
        assertThat(result.getToCurrency()).isEqualTo("EUR");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(88.880));
    }

    @Test
    void convertThisSameCurrencyShouldBeReturnExchangeDtoWithThisSameAmount() {
        //given
        var exchange = Exchange.builder().fromCurrency("PLN").toCurrency("PLN").amount(BigDecimal.valueOf(100)).build();
        var exchangeDtoSimple = new ExchangeDto(BigDecimal.valueOf(100), "PLN", "PLN", BigDecimal.valueOf(100));
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeDto(any(Exchange.class), any(BigDecimal.class))).thenReturn(exchangeDtoSimple);

        //when
        var result = currencyService.convertCurrency(exchange);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("PLN");
        assertThat(result.getToCurrency()).isEqualTo("PLN");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void convertCurrencyWhenFromIsPLNAndToIsAnotherCurrencyShouldBeReturnExchangeDtoWithConvertedAmount() {
        //given
        var exchange = Exchange.builder().fromCurrency("PLN").toCurrency("EUR").amount(BigDecimal.valueOf(100)).build();
        var exchangeDtoSimple = new ExchangeDto(new BigDecimal("100"), "PLN", "EUR", BigDecimal.valueOf(450));
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeDto(any(Exchange.class), any(BigDecimal.class))).thenReturn(exchangeDtoSimple);

        //when
        var result = currencyService.convertCurrency(exchange);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("PLN");
        assertThat(result.getToCurrency()).isEqualTo("EUR");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(450));
    }

    @Test
    void convertCurrencyWhenToIsPLNAndFromIsAnotherCurrencyShouldBeReturnExchangeDtoWithConvertedAmount() {
        //given
        var exchange = Exchange.builder().fromCurrency("EUR").toCurrency("PLN").amount(BigDecimal.valueOf(100)).build();
        var exchangeDtoSimple = new ExchangeDto(BigDecimal.valueOf(100), "EUR", "PLN", BigDecimal.valueOf(22.22));
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeDto(any(Exchange.class), any(BigDecimal.class))).thenReturn(exchangeDtoSimple);

        //when
        var result = currencyService.convertCurrency(exchange);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("EUR");
        assertThat(result.getToCurrency()).isEqualTo("PLN");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(22.22));
    }

    @Test
    void ifToCurrencyNoFoundShouldBeReturnNoCurrencyException() {
        var exchange = Exchange.builder().fromCurrency("EUR").toCurrency("waluta").amount(BigDecimal.valueOf(100)).build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);

        //when, then
        assertThatThrownBy(() -> currencyService.convertCurrency(exchange)).isInstanceOf(NoCurrencyException.class);
    }

    @Test
    void ifFromCurrencyNoFoundShouldBeReturnNoCurrencyException() {
        var exchange = Exchange.builder().fromCurrency("Waluta").toCurrency("EUR").amount(BigDecimal.valueOf(100)).build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);

        //when, then
        assertThatThrownBy(() -> currencyService.convertCurrency(exchange)).isInstanceOf(NoCurrencyException.class);
    }
}
