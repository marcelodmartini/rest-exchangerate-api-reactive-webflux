package com.coralogix.calculator.services;

import com.coralogix.calculator.controller.response.ExchangeRateResponse;
import com.coralogix.calculator.model.ExchangeRate;
import com.coralogix.calculator.repository.ExchangeRateRepository;
import com.coralogix.calculator.services.impl.ExchangeRateServiceImpl;
import com.coralogix.calculator.services.response.ApiCurrencyThirdPartyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTest {

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Mock
    private ApiCurrencyThirdPartyService apiCurrencyThirdPartyService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    private ExchangeRate exchangeRate;

    private ApiCurrencyThirdPartyResponse apiResponse;

    @BeforeEach
    public void setup() {
        exchangeRate = new ExchangeRate();
        exchangeRate.setDate("2023-08-01");
        exchangeRate.setOriginCurrency("USD");
        exchangeRate.setFinalCurrency("EUR");
        exchangeRate.setRateValue(Double.valueOf("0.85"));

        apiResponse = new ApiCurrencyThirdPartyResponse();
        apiResponse.setDate("2023-08-01");
        apiResponse.setBase("USD");
        apiResponse.setRates(Collections.singletonMap("EUR", Double.valueOf("0.85")));
    }

    @Test
    public void testGetExchangeRate() {
        when(exchangeRateRepository.findFirstByOriginCurrencyAndFinalCurrency(anyString(), anyString()))
                .thenReturn(Mono.empty());
        when(apiCurrencyThirdPartyService.getFixerLatest(anyString(), anyString()))
                .thenReturn(Mono.just(apiResponse));
        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenReturn(Mono.just(exchangeRate));

        StepVerifier.create(exchangeRateService.getExchangeRate("USD", "EUR"))
                .expectNextMatches(response -> response.getValue().equals("0.85"))
                .verifyComplete();
    }

    @Test
    public void testGetAllExchangeRate() {
        when(exchangeRateRepository.findAll())
                .thenReturn(Flux.just(exchangeRate));

        StepVerifier.create(exchangeRateService.getAllExchangeRate())
                .expectNextMatches(response -> response.getValue().equals("0.85"))
                .verifyComplete();
    }
}
