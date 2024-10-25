package com.yoxaron.cuurency_exchange.dto;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate
) {
}
