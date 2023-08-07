package com.coralogix.calculator.services.impl;

import com.coralogix.calculator.controller.response.ExchangeRateResponse;
import com.coralogix.calculator.model.ExchangeRate;
import com.coralogix.calculator.repository.ExchangeRateRepository;
import com.coralogix.calculator.services.ApiCurrencyThirdPartyService;
import com.coralogix.calculator.services.ExchangeRateService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ApiCurrencyThirdPartyService apiCurrencyThirdPartyService;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRateResponse> getExchangeRate(String originCurrency, String finalCurrency) {
    								  
        return exchangeRateRepository.findFirstByOriginCurrencyAndFinalCurrency(originCurrency, finalCurrency)
                .switchIfEmpty(Mono.defer(() ->
                        apiCurrencyThirdPartyService.getFixerLatest(originCurrency, finalCurrency)
                                .flatMap(apiResponse -> {
                                    ExchangeRate newExchangeRate = new ExchangeRate();
                                    newExchangeRate.setDate(apiResponse.getDate());
                                    newExchangeRate.setOriginCurrency(originCurrency);
                                    newExchangeRate.setFinalCurrency(finalCurrency);
                                    newExchangeRate.setRateValue(apiResponse.getRates().get(finalCurrency));
                                    return exchangeRateRepository.save(newExchangeRate);
                                })
                ))
                .map(ExchangeRateResponse::mapFromEntity)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<ExchangeRateResponse> getAllExchangeRate() {
        return exchangeRateRepository.findAll()
                .map(ExchangeRateResponse::mapFromEntity)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
