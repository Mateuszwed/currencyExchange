package com.mateuszwed.currencyExchange.client;

import com.mateuszwed.currencyExchange.dto.NBPDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.EmptyListException;
import com.mateuszwed.currencyExchange.exception.HttpException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NBPApiClientTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    NBPApiClient nbpApiClient;

    @Test
    void methodGetResponseFromNBPApiShouldBeReturnListOfNBPRateDtoAndStatusCode200() {
        // given
        var nbpRateDto = new NBPRateDto("", "USD", new BigDecimal("3.5"));
        var nbpDto = new NBPDto(List.of(nbpRateDto));
        var responseEntity = new ResponseEntity<>(List.of(nbpDto), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenReturn(responseEntity);

        // when
        var result = nbpApiClient.getResponseFromNBPApi("table");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("USD");
        assertThat(result.get(0).getMid()).isEqualTo(new BigDecimal("3.5"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }));
    }

    @Test
    void wrongUrlFromClientShouldBeReturnThrowHttpExceptionWithStatusCode404() {
        // given
        var httpClientErrorException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenThrow(httpClientErrorException);

        // when, then
        assertThrows(HttpException.class, () -> nbpApiClient.getResponseFromNBPApi("table"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }));
    }

    @Test
    void wrongDataFromServerShouldBeReturnThrowHttpExceptionWithStatusCode500() {
        // given
        var httpServerErrorException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenThrow(httpServerErrorException);

        // when, then
        assertThrows(HttpException.class, () -> nbpApiClient.getResponseFromNBPApi("table"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }));
    }

    @Test
    void whenMethodReturnEmptyListShouldBeThrowNullPointerException() {
        //given
        var responseEntity = ResponseEntity.ok(List.of(new NBPDto(List.of())));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenReturn(responseEntity);

        //when, then
        assertThatThrownBy(() -> nbpApiClient.getResponseFromNBPApi("table")).isInstanceOf(EmptyListException.class).hasMessage("List is empty");
    }

    @Test
    void whenRequestReturnNullThenThrowsNullPointerException() {
        //given
        ResponseEntity<List<NBPDto>> responseEntity = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenReturn(responseEntity);

        //when, then
        assertThatThrownBy(() -> nbpApiClient.getResponseFromNBPApi("table")).isInstanceOf(NullPointerException.class).hasMessage("Api response have null value");
    }
}