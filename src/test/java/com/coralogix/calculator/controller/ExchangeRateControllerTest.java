package com.coralogix.calculator.controller;

import com.coralogix.calculator.controller.response.ExchangeRateResponse;
import com.coralogix.calculator.services.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ExchangeRateControllerTest {

    private WebTestClient webTestClient;
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = Mockito.mock(ExchangeRateService.class);
        ExchangeRateController exchangeRateController = new ExchangeRateController(exchangeRateService);
        webTestClient = WebTestClient.bindToController(exchangeRateController).build();
    }

    @Test
    void getExchangeRate() {
        ExchangeRateResponse exchangeRateResponse = ExchangeRateResponse.builder()
                .id(1L)
                .originCurrency("USD")
                .finalCurrency("EUR")
                .date("2023-08-01")
                .value("0.85")
                .success(true)
                .build();

        when(exchangeRateService.getExchangeRate(anyString(), anyString())).thenReturn(Mono.just(exchangeRateResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/exchange-rates/single")
                        .queryParam("originCurrency", "USD")
                        .queryParam("finalCurrency", "EUR")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.originCurrency").isEqualTo("USD")
                .jsonPath("$.finalCurrency").isEqualTo("EUR")
                .jsonPath("$.date").isEqualTo("2023-08-01")
                .jsonPath("$.value").isEqualTo("0.85")
                .jsonPath("$.success").isEqualTo(true);
    }

    @Test
    void getAllExchangeRate() {
        ExchangeRateResponse exchangeRateResponse1 = ExchangeRateResponse.builder()
                .id(1L)
                .originCurrency("USD")
                .finalCurrency("EUR")
                .date("2023-08-01")
                .value("0.85")
                .success(true)
                .build();

        ExchangeRateResponse exchangeRateResponse2 = ExchangeRateResponse.builder()
                .id(2L)
                .originCurrency("GBP")
                .finalCurrency("EUR")
                .date("2023-08-01")
                .value("1.17")
                .success(true)
                .build();

        when(exchangeRateService.getAllExchangeRate()).thenReturn(Flux.just(exchangeRateResponse1, exchangeRateResponse2));

        webTestClient.get()
                .uri("/api/exchange-rates/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRateResponse.class)
                .hasSize(2)
                .contains(exchangeRateResponse1, exchangeRateResponse2);
    }
}

