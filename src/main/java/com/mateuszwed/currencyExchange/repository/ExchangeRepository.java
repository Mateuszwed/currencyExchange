package com.mateuszwed.currencyExchange.repository;

import com.mateuszwed.currencyExchange.model.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeEntity, Long> {
}
