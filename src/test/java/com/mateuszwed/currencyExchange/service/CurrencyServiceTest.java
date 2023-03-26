package com.mateuszwed.currencyExchange.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.NoCurrencyException;
import com.mateuszwed.currencyExchange.mapper.ExchangeMapper;
import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import com.mateuszwed.currencyExchange.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    NBPApiClient nbpApiClient;
    @Mock
    ExchangeMapper exchangeMapper;
    @Mock
    ExchangeRepository exchangeRepository;
    @InjectMocks
    CurrencyService currencyService;

    @Test
    void convertCurrencyWhenFromAndToAreDifferentShouldBeReturnExchangeDtoWithConvertedAmount() {
        //given
        var fromCurrency = "USD";
        var toCurrency = "EUR";
        var exchangeDto = buildExchangeDto(fromCurrency, toCurrency);
        var convertedAmount = new BigDecimal("88.88");
        var exchangeEntity = buildExchangeEntity(fromCurrency, toCurrency, convertedAmount);
        var exchangeRateDto = buildExchangeRateDto(fromCurrency, toCurrency, convertedAmount);
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);
        when(exchangeMapper.toEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.toDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("USD");
        assertThat(result.getToCurrency()).isEqualTo("EUR");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(88.88));
    }

    @Test
    void convertThisSameCurrencyShouldBeReturnExchangeDtoWithThisSameAmount() {
        //given
        var currency = "PLN";
        var exchangeDto = buildExchangeDto(currency, currency);
        var convertedAmount = new BigDecimal("100.00");
        var exchangeEntity = buildExchangeEntity(currency, currency, convertedAmount);
        var exchangeRateDto = buildExchangeRateDto(currency, currency, convertedAmount);
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);
        when(exchangeMapper.toEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.toDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

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
        var fromCurrency = "PLN";
        var toCurrency = "EUR";
        var exchangeDto = buildExchangeDto(fromCurrency, toCurrency);
        var convertedAmount = new BigDecimal("22.22");
        var exchangeEntity = buildExchangeEntity(fromCurrency, toCurrency, convertedAmount);
        var exchangeRateDto = buildExchangeRateDto(fromCurrency, toCurrency, convertedAmount);
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);
        when(exchangeMapper.toEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.toDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("PLN");
        assertThat(result.getToCurrency()).isEqualTo("EUR");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(22.22));
    }

    @Test
    void convertCurrencyWhenToIsPLNAndFromIsAnotherCurrencyShouldBeReturnExchangeDtoWithConvertedAmount() {
        //given
        var fromCurrency = "EUR";
        var toCurrency = "PLN";
        var exchangeDto = buildExchangeDto(fromCurrency, toCurrency);
        var convertedAmount = new BigDecimal("450.00");
        var exchangeEntity = buildExchangeEntity(fromCurrency, toCurrency, convertedAmount);
        var exchangeRateDto = buildExchangeRateDto(fromCurrency, toCurrency, convertedAmount);
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);
        when(exchangeMapper.toEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.toDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("EUR");
        assertThat(result.getToCurrency()).isEqualTo("PLN");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(450.00));
    }

    @Test
    void ifToCurrencyNoFoundShouldBeReturnNoCurrencyException() {
        //given
        var exchange = ExchangeDto.builder().fromCurrency("EUR").toCurrency("waluta").amount(BigDecimal.valueOf(100)).build();
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);

        //when, then
        assertThatThrownBy(() -> currencyService.convertCurrency(exchange)).isInstanceOf(NoCurrencyException.class);
    }

    @Test
    void ifFromCurrencyNoFoundShouldBeReturnNoCurrencyException() {
        //given
        var exchange = ExchangeDto.builder().fromCurrency("Waluta").toCurrency("EUR").amount(BigDecimal.valueOf(100)).build();
        var nbpRateList = new ArrayList<NBPRateDto>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(any())).thenReturn(nbpRateList);

        //when, then
        assertThatThrownBy(() -> currencyService.convertCurrency(exchange)).isInstanceOf(NoCurrencyException.class);
    }

    private ExchangeDto buildExchangeDto(String from, String to) {
        return ExchangeDto.builder()
            .fromCurrency(from)
            .toCurrency(to)
            .amount(BigDecimal.valueOf(100))
            .build();
    }

    private ExchangeEntity buildExchangeEntity(String from, String to, BigDecimal convertedAmount) {
        return ExchangeEntity.builder()
            .fromCurrency(from)
            .toCurrency(to)
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
    }

    private ExchangeRateDto buildExchangeRateDto(String from, String to, BigDecimal convertedAmount) {
        return ExchangeRateDto.builder()
            .fromCurrency(from)
            .toCurrency(to)
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
    }
}

