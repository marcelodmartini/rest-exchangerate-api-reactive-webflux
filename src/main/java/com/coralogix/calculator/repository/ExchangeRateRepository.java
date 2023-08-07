package com.coralogix.calculator.repository;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.coralogix.calculator.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, Long> {

   @Query("SELECT PUBLIC.EXCHANGE_RATE.* FROM PUBLIC.EXCHANGE_RATE WHERE origin_currency = :originCurrency AND final_currency = :finalCurrency LIMIT 1")
   Mono<ExchangeRate> findFirstByOriginCurrencyAndFinalCurrency(String originCurrency, String finalCurrency);
}
