package com.coralogix.calculator.services;

import com.coralogix.calculator.controller.response.ExchangeRateResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for managing exchange rates.
 */
public interface ExchangeRateService {
	
    /**
     * Get the exchange rate for a specific currency pair.
     * 
     * @param originCurrency The currency to convert from.
     * @param finalCurrency The currency to convert to.
     * @return A Mono emitting the exchange rate or an error signal if the exchange rate could not be found.
     * 
     * Both originCurrency and finalCurrency should be non-null and non-empty. They should represent valid ISO currency codes.
     */
	Mono<ExchangeRateResponse> getExchangeRate(String originCurrency, String finalCurrency);

    /**
     * Get all available exchange rates.
     * 
     * @return A Flux emitting all available exchange rates or an error signal if the exchange rates could not be retrieved.
     */
	Flux<ExchangeRateResponse> getAllExchangeRate();
}
