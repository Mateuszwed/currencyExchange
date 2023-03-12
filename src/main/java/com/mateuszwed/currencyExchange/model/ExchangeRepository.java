package com.mateuszwed.currencyExchange.model;

import java.util.List;

public interface ExchangeRepository {

    ExchangeEntity save(ExchangeEntity exchangeEntity);
    List<ExchangeEntity> findAll();
}
