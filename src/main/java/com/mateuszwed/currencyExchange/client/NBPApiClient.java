package com.mateuszwed.currencyExchange.client;

import com.mateuszwed.currencyExchange.exception.EmptyListException;
import com.mateuszwed.currencyExchange.exception.HttpException;
import com.mateuszwed.currencyExchange.dto.NBPDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Log
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NBPApiClient {
    RestTemplate restTemplate;

    public List<NBPRateDto> getResponseFromNBPApi(String table) {
        ResponseEntity<List<NBPDto>> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            response = restTemplate.exchange(table, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warning(e.getStatusCode().toString());
            throw new HttpException("Problem with call to NPB API, status code: " + e.getStatusCode());
        }
        log.info(response.getStatusCode().toString());

        Optional<List<NBPDto>> nbpDtoOptional = Optional.ofNullable(response.getBody());
        nbpDtoOptional.orElseThrow(()-> new NullPointerException("Response is null"));

        List<NBPRateDto> rates = nbpDtoOptional
                .stream()
                .flatMap(Collection::stream)
                .map(NBPDto::getRates)
                .flatMap(Collection::stream)
                .toList();
        if(rates.isEmpty()) {
            throw new EmptyListException("List is empty");
        }
        return rates;
    }
}