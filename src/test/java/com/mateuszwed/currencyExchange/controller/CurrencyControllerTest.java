package com.mateuszwed.currencyExchange.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateuszwed.currencyExchange.dto.ExchangeDto;
import com.mateuszwed.currencyExchange.dto.ExchangeRateDto;
import com.mateuszwed.currencyExchange.repository.ExchangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
class CurrencyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ExchangeRepository exchangeRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void postCorrectedExchangeShouldBeSaveConvertedExchangeToDBAndReturnStatus200() throws Exception {
        //given, when
        var exchangeDto = buildCorrectedExampleDto();
        MvcResult result = mockMvc.perform(post("/exchanges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exchangeDto))
                .characterEncoding("utf-8"))
            .andDo(print())
            .andReturn();
        String json = result.getResponse().getContentAsString();
        ExchangeRateDto exchangeRateDto = objectMapper.readValue(json, ExchangeRateDto.class);

        //then
        assertThat(exchangeRateDto.getAmount()).isEqualTo(BigDecimal.valueOf(20.0));
        assertThat(exchangeRateDto.getFromCurrency()).isEqualTo("PLN");
        assertThat(exchangeRateDto.getToCurrency()).isEqualTo("USD");
        assertThat(exchangeRateDto.getConvertedAmount()).isNotNull();

    }

    @Test
    void postUncorrectedCurrencyCodeShouldBeReturnStatus400() throws Exception {
        //given, when
        var exchangeDto = buildUncorrectedExampleDto();
        mockMvc.perform(post("/exchanges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exchangeDto))
                .characterEncoding("utf-8"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

        var exchangeEntity = exchangeRepository.findAll();

        //then
        assertThat(exchangeEntity).isEmpty();
    }

    private ExchangeDto buildCorrectedExampleDto() {
        return ExchangeDto.builder()
            .amount(BigDecimal.valueOf(20.0))
            .fromCurrency("PLN")
            .toCurrency("USD")
            .build();
    }

    private ExchangeDto buildUncorrectedExampleDto() {
        return ExchangeDto.builder()
            .amount(BigDecimal.valueOf(20))
            .fromCurrency("12345")
            .toCurrency("USD")
            .build();
    }
}