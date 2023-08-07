package com.coralogix.calculator.controller.response;

import com.coralogix.calculator.model.ExchangeRate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private Long id;
    private String originCurrency;
    private String finalCurrency;
    private String date;
    private String value;
    private boolean success;

    public static ExchangeRateResponse mapFromEntity(ExchangeRate entity) {
        return ExchangeRateResponse.builder()
                .id(entity.getId())
                .originCurrency(entity.getOriginCurrency())
                .finalCurrency(entity.getFinalCurrency())
                .date(entity.getDate())
                .value(entity.getRateValue().toString())
                .success(true)
                .build();
    }
}
