package com.mateuszwed.currencyExchange.repository;

import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepository extends JpaRepository<ExchangeEntity, Long> {
}
