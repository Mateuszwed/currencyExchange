package com.mateuszwed.currencyExchange.client;

import com.mateuszwed.currencyExchange.dto.NBPDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.HttpException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NBPApiClient {
    RestTemplate restTemplate;

    public List<NBPRateDto> getResponseFromNBPApi(String table) {
        try {
            ResponseEntity<List<NBPDto>> response = restTemplate.exchange(table, HttpMethod.GET, null, new ParameterizedTypeReference<List<NBPDto>>() {
            });
            log.info(response.getStatusCode().toString());
            return response.getBody().get(0).getRates();
        } catch (HttpClientErrorException e) {
            log.warning(e.getStatusCode().toString());
            throw new HttpException("Problem with call to NPB API, status code: " + e.getStatusCode());
        }
    }
}