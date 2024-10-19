package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.dto.CurrencyDto;
import com.yoxaron.cuurency_exchange.model.Currency;

public class ModelMapper {

    public static Currency toCurrency(CurrencyDto obj) {
        if (obj == null) return null;
        return new Currency(obj.id(), obj.code(), obj.name(), obj.sign());
    }

    public static CurrencyDto toCurrencyDto(Currency obj) {
        if (obj == null) return null;
        return new CurrencyDto(obj.getId(), obj.getCode(), obj.getFullName(), obj.getSign());
    }
}
