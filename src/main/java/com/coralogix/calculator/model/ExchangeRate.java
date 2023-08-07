package com.coralogix.calculator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
@Table("PUBLIC.EXCHANGE_RATE")
public class ExchangeRate {

    @Id
    private Long id;

    @Column("origin_currency")
    private String originCurrency;

    @Column("final_currency")
    private String finalCurrency;

    @Column("date")
    private String date;

    @Column("rate_value")
    private Double rateValue;
}
