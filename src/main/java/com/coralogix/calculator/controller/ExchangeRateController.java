package com.coralogix.calculator.controller;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.coralogix.calculator.controller.response.ExchangeRateResponse;
import com.coralogix.calculator.services.ExchangeRateService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

   private final ExchangeRateService exchangeRateService;

   @GetMapping("/single")
   public Mono<ExchangeRateResponse> getExchangeRate(@RequestParam("originCurrency") String originCurrency,
         @RequestParam("finalCurrency") String finalCurrency) {
      return exchangeRateService.getExchangeRate(originCurrency, finalCurrency)
              .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange rate not found")));
   }

   @GetMapping("/all")
   public Flux<ExchangeRateResponse> getAllExchangeRate() {
      return exchangeRateService.getAllExchangeRate();
   }
}
