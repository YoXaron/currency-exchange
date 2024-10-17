package com.yoxaron.cuurency_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}
