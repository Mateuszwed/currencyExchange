package com.mateuszwed.currencyExchange.repository;

import com.mateuszwed.currencyExchange.model.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateEntity, Integer> {
}
