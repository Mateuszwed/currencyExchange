package com.mateuszwed.currencyExchange.service;

import com.mateuszwed.currencyExchange.client.NBPApiClient;
import com.mateuszwed.currencyExchange.exception.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.exception.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.model.Exchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    NBPApiClient nbpApiClient;
    @InjectMocks
    CurrencyService currencyService;

    @Test
    void convertCurrencyShouldBeReturnExchangeDto() {
        //given
        Exchange exchange = Exchange.builder().fromCurrency("USD").toCurrency("EUR").amount(new BigDecimal("100")).build();
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Dolar", "USD", BigDecimal.valueOf(4.0));
        NBPRateDto nbpRateDto1 = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        nbpRateList.add(nbpRateDto1);
        when(nbpApiClient.getResponseFromNBPApi(null)).thenReturn(nbpRateList);
        //when
        ExchangeDto result = currencyService.convertCurrency(exchange);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getFromCurrency()).isEqualTo("USD");
        assertThat(result.getToCurrency()).isEqualTo("EUR");
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(BigDecimal.valueOf(88.880));
    }

    @Test
    void methodConvertToPlnShouldBeReturnConvertedAnyCurrencyToPln() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        //when
        BigDecimal result = currencyService.convertToPln("EUR", new BigDecimal("100"), nbpRateList);
        //then
        assertThat(result).isEqualTo(new BigDecimal("450.0"));
    }

    @Test
    void methodConvertFromPlnShouldBeReturnConvertedToAnyCurrency() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        //when
        BigDecimal result = currencyService.convertFromPln("EUR", new BigDecimal("100"), nbpRateList);
        //then
        assertThat(result).isEqualTo(new BigDecimal("22.22"));
    }

    @Test
    void methodConvertToPlnShouldThrowsIllegalArgumentExceptionWhenCurrencyDoesNotExist() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        //when, then
        assertThatThrownBy(() -> currencyService.convertToPln("Euro", new BigDecimal("100"), nbpRateList)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void methodConvertFromPlnShouldThrowsIllegalArgumentExceptionWhenCurrencyDoesNotExist() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        //when, then
        assertThatThrownBy(() -> currencyService.convertFromPln("Euro", new BigDecimal("100"), nbpRateList)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void methodConvertShouldBeReturnAmountWhenCurrenciesAreTheSame() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        String fromCurrency = "PLN";
        String toCurrency = "PLN";
        BigDecimal amount = new BigDecimal("100");
        //when
        BigDecimal result = currencyService.convert(fromCurrency, toCurrency, amount, nbpRateList);
        //then
        assertThat(result).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void methodConvertWhenFromCurrencyIsEqualsPLNShouldBeReturnConvertFromPLNValue() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        String fromCurrency = "PLN";
        String toCurrency = "EUR";
        BigDecimal amount = new BigDecimal("100");
        //when
        BigDecimal result = currencyService.convert(fromCurrency, toCurrency, amount, nbpRateList);
        //then
        assertThat(result).isEqualTo(new BigDecimal("22.22"));
    }

    @Test
    void methodConvertWhenToCurrencyIsEqualsPLNShouldBeReturnConvertToPLNValue() {
        //given
        List<NBPRateDto> nbpRateList = new ArrayList<>();
        NBPRateDto nbpRateDto = new NBPRateDto("Euro", "EUR", BigDecimal.valueOf(4.5));
        nbpRateList.add(nbpRateDto);
        String fromCurrency = "EUR";
        String toCurrency = "PLN";
        BigDecimal amount = new BigDecimal("100");
        //when
        BigDecimal result = currencyService.convert(fromCurrency, toCurrency, amount, nbpRateList);
        //then
        assertThat(result).isEqualTo(new BigDecimal("450.0"));
    }
}