package com.mateuszwed.currencyExchange.model;

public interface ExchangeRepository {
    ExchangeEntity save(ExchangeEntity exchangeEntity);
}
