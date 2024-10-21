package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.dto.CurrencyDto;
import com.yoxaron.cuurency_exchange.dto.ExchangeRateDto;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;

public class ModelMapper {

    public static Currency toCurrency(CurrencyDto obj) {
        if (obj == null) return null;
        return new Currency(obj.id(), obj.code(), obj.name(), obj.sign());
    }

    public static CurrencyDto toCurrencyDto(Currency obj) {
        if (obj == null) return null;
        return new CurrencyDto(obj.getId(), obj.getCode(), obj.getFullName(), obj.getSign());
    }

    public static ExchangeRate toExchangeRate(ExchangeRateDto obj) {
        if (obj == null) return null;
        return new ExchangeRate(
                obj.id(),
                toCurrency(obj.baseCurrency()),
                toCurrency(obj.targetCurrency()),
                obj.rate()
        );
    }

    public static ExchangeRateDto toExchangeRateDto(ExchangeRate obj) {
        if (obj == null) return null;
        return new ExchangeRateDto(
                obj.getId(),
                toCurrencyDto(obj.getBaseCurrency()),
                toCurrencyDto(obj.getTargetCurrency()),
                obj.getRate()
        );
    }
}
