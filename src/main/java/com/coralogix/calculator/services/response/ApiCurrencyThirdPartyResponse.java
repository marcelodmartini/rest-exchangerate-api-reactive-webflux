package com.coralogix.calculator.services.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {
 * 	"success": true,
 * 	"timestamp": 1683733305,
 * 	"base": "USD",
 * 	"date": "2023-05-10",
 * 	"rates": {
 * 		"EUR": 0.95798
 *      }
 * }
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCurrencyThirdPartyResponse {

   private boolean success;
   private Long timestamp;
   private String base;
   private String date;
   @Builder.Default
   private Map<String, Double> rates = new LinkedHashMap<>();
}
