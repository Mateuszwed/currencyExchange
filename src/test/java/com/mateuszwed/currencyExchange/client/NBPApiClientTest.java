package com.mateuszwed.currencyExchange.client;

import com.mateuszwed.currencyExchange.exception.dto.NBPDto;
import com.mateuszwed.currencyExchange.exception.dto.NBPRateDto;
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
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
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
        NBPDto nbpDto = new NBPDto();
        NBPRateDto nbpRateDto = new NBPRateDto();
        nbpRateDto.setCode("USD");
        nbpRateDto.setMid(new BigDecimal("3.5"));
        nbpDto.setRates(List.of(nbpRateDto));
        ResponseEntity<List<NBPDto>> responseEntity = new ResponseEntity<>(List.of(nbpDto), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenReturn(responseEntity);

        // when
        List<NBPRateDto> result = nbpApiClient.getResponseFromNBPApi("table");

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getCode()).isEqualTo("USD");
        assertThat(result.get(0).getMid()).isEqualTo("3.5");
        assertThat(result).hasSize(1);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }));
    }

    @Test
    void wrongUrlFromClientShouldBeReturnThrowHttpExceptionWithStatusCode404() {
        // given
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
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
        HttpServerErrorException httpServerErrorException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }))).thenThrow(httpServerErrorException);

        // when, then
        assertThrows(HttpException.class, () -> nbpApiClient.getResponseFromNBPApi("table"));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(new ParameterizedTypeReference<List<NBPDto>>() {
        }));
    }

    @Test
    void whenMethodReturnEmptyListShouldBeThrowNullPointerException(){
        //given
        List<NBPDto> nbpDtoList = null;
        ResponseEntity<List<NBPDto>> responseEntity = new ResponseEntity<>(nbpDtoList, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Throwable thrown = catchThrowable(() -> nbpApiClient.getResponseFromNBPApi("table"));

        assertThat(thrown).isInstanceOf(RuntimeException.class);
        assertThat(thrown.getMessage()).isEqualTo("Method getResponseFromNBPApi return empty list");
    }
}