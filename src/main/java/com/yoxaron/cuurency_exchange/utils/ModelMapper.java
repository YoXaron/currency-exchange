package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.dto.CurrencyDto;
import com.yoxaron.cuurency_exchange.dto.ExchangeRateDto;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ModelMapper {

    public static Currency toCurrency(CurrencyDto obj) {
        if (obj == null) return null;
        return new Currency(obj.id(), obj.code(), obj.name(), obj.sign());
    }

    public static CurrencyDto toCurrencyDto(Currency obj) {
        if (obj == null) return null;
        return new CurrencyDto(obj.getId(), obj.getCode(), obj.getFullName(), obj.getSign());
    }

    public static ExchangeRateDto toExchangeRateDto(ExchangeRate obj) {
        if (obj == null) return null;
        return new ExchangeRateDto(
                obj.getId(),
                toCurrencyDto(obj.getBaseCurrency()),
                toCurrencyDto(obj.getTargetCurrency()),
                roundToSignificantFigures(obj.getRate())
        );
    }

    private static BigDecimal roundToSignificantFigures(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        int scale = 2 - value.precision() + value.scale();
        scale = Math.max(2, scale);
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
