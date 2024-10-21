package com.yoxaron.cuurency_exchange.dto;

public record CurrencyDto(
        Long id,
        String code,
        String name,
        String sign
) {
}
