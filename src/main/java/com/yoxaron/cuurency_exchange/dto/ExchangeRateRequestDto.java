package com.yoxaron.cuurency_exchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExchangeRateRequestDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
}
