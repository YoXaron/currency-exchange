package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateRequestDto;
import com.yoxaron.cuurency_exchange.dto.ExchangeRequestDto;
import com.yoxaron.cuurency_exchange.exception.BadRequestException;
import com.yoxaron.cuurency_exchange.model.Currency;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

public class PathParser {

    public static String extractCurrencyCode(HttpServletRequest request) throws BadRequestException {
        var pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 4 && pathInfo.startsWith("/")) {
            var currencyCode = pathInfo.substring(1).toUpperCase();
            if (isValidCode(currencyCode)) {
                return currencyCode;
            }
        }
        throw new BadRequestException();
    }

    public static Currency extractCurrency(HttpServletRequest request) throws BadRequestException {
        var name = request.getParameter("name");
        var code = request.getParameter("code");
        var sign = request.getParameter("sign");

        if (isValidField(name) && isValidCode(code) && isValidField(sign)) {
            return new Currency(null, code, name, sign);
        }
        throw new BadRequestException();
    }

    public static ExchangeRateRequestDto extractCurrencyPairCodes(HttpServletRequest request) throws BadRequestException {
        var pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 7 && pathInfo.startsWith("/")) {
            var baseCurrencyCode = pathInfo.substring(1, 4).toUpperCase();
            var targetCurrencyCode = pathInfo.substring(4).toUpperCase();

            if (isValidCode(baseCurrencyCode) && isValidCode(targetCurrencyCode)) {
                return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, null);
            }
        }
        throw new BadRequestException();
    }

    public static ExchangeRateRequestDto extractExchangeRateRequestDto(HttpServletRequest request) throws BadRequestException {
        var baseCurrencyCode = request.getParameter("baseCurrencyCode");
        var targetCurrencyCode = request.getParameter("targetCurrencyCode");
        var rate = request.getParameter("rate");

        if (isValidCode(baseCurrencyCode) && isValidCode(targetCurrencyCode) && isValidRate(rate)) {
            return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
        }
        throw new BadRequestException();
    }

    public static ExchangeRateRequestDto extractExchangeRateUpdateRequestDto(HttpServletRequest request) throws BadRequestException {
        var requestDto = extractCurrencyPairCodes(request);
        var rate = request.getParameter("rate");
        if (isValidRate(rate)) {
            requestDto.setRate(new BigDecimal(rate));
            return requestDto;
        }
        throw new BadRequestException();
    }

    public static ExchangeRequestDto extractExchangeRequestDto(HttpServletRequest request) throws BadRequestException {
        var from = request.getParameter("from");
        var to = request.getParameter("to");
        var amountParam = request.getParameter("amount");

        if (isValidCode(from) && isValidCode(to) && isValidField(amountParam)) {
            try {
                var amount = new BigDecimal(amountParam);
                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    throw new BadRequestException();
                }
                return new ExchangeRequestDto(from, to, amount);
            } catch (NumberFormatException e) {
                throw new BadRequestException();
            }
        }
        throw new BadRequestException();
    }

    private static boolean isValidCode(String code) {
        if (code != null && code.toUpperCase().matches("[A-Z]{3}")) {
            return true;
        }
        throw new BadRequestException();
    }

    private static boolean isValidRate(String rate) {
        try {
            if (rate != null && new BigDecimal(rate).compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
        throw new BadRequestException();
    }

    private static boolean isValidField(String name) {
        if (name != null && !name.isBlank()) {
            return true;
        }
        throw new BadRequestException();
    }
}
