package com.mateuszwed.currencyExchange.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.mateuszwed.currencyExchange.model.SqlExchangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CurrencyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    SqlExchangeRepository sqlExchangeRepository;

    @Test
    void postCorrectedExchangeShouldBeSaveConvertedExchangeToDBAndReturnStatus200() throws Exception {
        //given, when
        mockMvc.perform(post("/exchanges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 20, \"fromCurrency\": \"PLN\", \"toCurrency\": \"USD\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
        var exchangeEntity = sqlExchangeRepository.findAll();

        //then
        assertThat(exchangeEntity).hasSize(1);
        assertThat(exchangeEntity.get(0).getId()).isEqualTo(1L);
        assertThat(exchangeEntity.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(20));
        assertThat(exchangeEntity.get(0).getFromCurrency()).isEqualTo("PLN");
        assertThat(exchangeEntity.get(0).getToCurrency()).isEqualTo("USD");
        assertThat(exchangeEntity.get(0).getConvertedAmount()).isNotNull();
    }

    @Test
    void postUncorrectedCurrencyCodeShouldBeReturnStatus400() throws Exception {
        //given, when
        mockMvc.perform(post("/exchanges")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 20, \"fromCurrency\": \"WALUTA\", \"toCurrency\": \"USD\"}"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

        var exchangeEntity = sqlExchangeRepository.findAll();

        //then
        assertThat(exchangeEntity).hasSize(0);
    }
}