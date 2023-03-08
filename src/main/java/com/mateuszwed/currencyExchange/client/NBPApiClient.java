package com.mateuszwed.currencyExchange.client;

import com.mateuszwed.currencyExchange.dto.NBPDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.EmptyListException;
import com.mateuszwed.currencyExchange.exception.HttpException;
import com.mateuszwed.currencyExchange.exception.NullResponseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NBPApiClient {
    RestTemplate restTemplate;

    public List<NBPRateDto> getResponseFromNBPApi(String table) {
        ResponseEntity<List<NBPDto>> response;
        var request = new HttpEntity<>(getHttpHeaders());
        try {
            response = restTemplate.exchange(table, HttpMethod.GET, request, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new HttpException(e.getStatusCode(), "Problem with call to NPB API");
        }
        var nbpDtoOptional = Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new NullResponseException("Api response have null value"));
        var rates = nbpDtoOptional.stream()
                .flatMap(nbpDto -> nbpDto.getRates().stream())
                .collect(Collectors.toList());
        if (rates.isEmpty()) {
            throw new EmptyListException("List is empty");
        }
        return rates;
    }

    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}