package com.yoxaron.cuurency_exchange.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExchangeRequestDto {
    private String from;
    private String to;
    private BigDecimal amount;
}
