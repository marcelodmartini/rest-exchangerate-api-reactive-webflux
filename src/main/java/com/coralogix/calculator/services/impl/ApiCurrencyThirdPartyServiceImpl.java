package com.coralogix.calculator.services.impl;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.coralogix.calculator.services.ApiCurrencyThirdPartyService;
import com.coralogix.calculator.services.response.ApiCurrencyThirdPartyResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
	
@Service
@RequiredArgsConstructor
public class ApiCurrencyThirdPartyServiceImpl implements ApiCurrencyThirdPartyService {

    @Value("${app.endpoints.api.rate}")
    private String endpointRate;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.create();
    }

    @Override
    public Mono<ApiCurrencyThirdPartyResponse> getFixerLatest(String originCurrency, String finalCurrency) {
        String url = endpointRate.replace("$MO", originCurrency).replace("$MD", finalCurrency);

        return webClient.get()
                .uri(URI.create(url))
                .retrieve()
                .bodyToMono(ApiCurrencyThirdPartyResponse.class);
    }
}
