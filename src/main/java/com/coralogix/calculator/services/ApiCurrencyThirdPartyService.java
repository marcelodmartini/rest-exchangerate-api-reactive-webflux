package com.coralogix.calculator.services;

import com.coralogix.calculator.services.response.ApiCurrencyThirdPartyResponse;
import reactor.core.publisher.Mono;

/**
 * This service interfaces with a third-party API to fetch the latest exchange rates.
 */
public interface ApiCurrencyThirdPartyService {

	/**
	 * Fetches the latest exchange rate for the given origin and final currencies from the Fixer API.
	 *
	 * @param originCurrency The ISO code of the origin currency.
	 * @param finalCurrency The ISO code of the final currency.
	 * @return A Mono that, when subscribed, emits the latest exchange rate or an error if the rate could not be fetched.
	 */
    Mono<ApiCurrencyThirdPartyResponse> getFixerLatest(String originCurrency, String finalCurrency);
}
