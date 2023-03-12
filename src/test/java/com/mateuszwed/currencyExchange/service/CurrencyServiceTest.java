package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.dto.ExchangeMapper;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.NoCurrencyException;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import com.mateuszwed.currencyExchange.model.ExchangeRepository;
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
import static org.mockito.Mockito.when;

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
        var exchangeDto = ExchangeDto.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .build();
        var convertedAmount = new BigDecimal("88.880");
        var exchangeEntity = ExchangeEntity.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        var exchangeRateDto = ExchangeRateDto.builder()
            .fromCurrency("USD")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.exchangeEntityToExchangeDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

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
        var exchangeDto = ExchangeDto.builder()
            .fromCurrency("PLN")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .build();
        var convertedAmount = new BigDecimal("100");
        var exchangeEntity = ExchangeEntity.builder()
            .fromCurrency("PLN")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        var exchangeRateDto = ExchangeRateDto.builder()
            .fromCurrency("PLN")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.exchangeEntityToExchangeDto(exchangeEntity)).thenReturn(exchangeRateDto);

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
        var exchangeDto = ExchangeDto.builder()
            .fromCurrency("PLN")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .build();
        var convertedAmount = new BigDecimal("22.22");
        var exchangeEntity = ExchangeEntity.builder()
            .fromCurrency("PLN")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        var exchangeRateDto = ExchangeRateDto.builder()
            .fromCurrency("PLN")
            .toCurrency("EUR")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.exchangeEntityToExchangeDto(exchangeEntity)).thenReturn(exchangeRateDto);

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
        var exchangeDto = ExchangeDto.builder()
            .fromCurrency("EUR")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .build();
        var convertedAmount = new BigDecimal("450.0");
        var exchangeEntity = ExchangeEntity.builder()
            .fromCurrency("EUR")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        var exchangeRateDto = ExchangeRateDto.builder()
            .fromCurrency("EUR")
            .toCurrency("PLN")
            .amount(BigDecimal.valueOf(100))
            .convertedAmount(convertedAmount)
            .build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        var nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        var nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        when(exchangeMapper.exchangeToExchangeEntity(exchangeDto, convertedAmount)).thenReturn(exchangeEntity);
        when(exchangeRepository.save(exchangeEntity)).thenReturn(exchangeEntity);
        when(exchangeMapper.exchangeEntityToExchangeDto(exchangeEntity)).thenReturn(exchangeRateDto);

        //when
        var result = currencyService.convertCurrency(exchangeDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("EUR");
        assertThat(result.getToCurrency()).isEqualTo("PLN");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(450.0));
    }

    @Test
    void ifToCurrencyNoFoundShouldBeReturnNoCurrencyException() {
        var exchange = ExchangeDto.builder().fromCurrency("EUR").toCurrency("waluta").amount(BigDecimal.valueOf(100)).build();
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
        var exchange = ExchangeDto.builder().fromCurrency("Waluta").toCurrency("EUR").amount(BigDecimal.valueOf(100)).build();
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

