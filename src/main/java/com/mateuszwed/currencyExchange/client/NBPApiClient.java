package com.mateuszwed.currencyExchange.client;
import com.mateuszwed.currencyExchange.dto.NBPDto;
import com.mateuszwed.currencyExchange.dto.NBPRateDto;
import com.mateuszwed.currencyExchange.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Component
@Log
@RequiredArgsConstructor
public class NBPApiClient {
    private final RestTemplate restTemplate;

    private NBPDto[] getResponseFromNBPApi() {
        ResponseEntity<NBPDto[]> response;
        Integer responseStatus = null;
        try {
            String url = "http://api.nbp.pl/api/exchangerates/tables/a";
            response = restTemplate.getForEntity(url, NBPDto[].class);
            responseStatus = response.getStatusCodeValue();
            log.info("status code: " + responseStatus);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new HttpException(e.getStatusCode());
        }
    }
    public List<NBPRateDto> getCurrency() {
        List<NBPDto> list = Arrays.asList(getResponseFromNBPApi());
        return list.get(0).getRates();
    }
}