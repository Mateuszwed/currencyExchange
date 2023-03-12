package com.mateuszwed.currencyExchange.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlExchangeRepository extends ExchangeRepository, JpaRepository<ExchangeEntity, Long> {
}
