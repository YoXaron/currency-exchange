package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateRequestDto;
import com.yoxaron.cuurency_exchange.exception.BadRequestException;
import com.yoxaron.cuurency_exchange.model.Currency;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

public class PathParser {

    public static String extractCurrencyCode(HttpServletRequest request) throws BadRequestException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 4 && pathInfo.startsWith("/")) {
            String currencyCode = pathInfo.substring(1).toUpperCase();
            if (isValidCode(currencyCode)) {
                return currencyCode;
            }
        }
        throw new BadRequestException();
    }

    public static Currency extractCurrency(HttpServletRequest request) throws BadRequestException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (isValidField(name) || isValidCode(code) || isValidField(sign)) {
            return new Currency(null, code, name, sign);
        }
        throw new BadRequestException();
    }

    public static String[] extractCurrencyPairCodes(HttpServletRequest request) throws BadRequestException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 7 && pathInfo.startsWith("/")) {
            String baseCurrencyCode = pathInfo.substring(1, 4).toUpperCase();
            String targetCurrencyCode = pathInfo.substring(4).toUpperCase();

            if (isValidCode(baseCurrencyCode) && isValidCode(targetCurrencyCode)) {
                return new String[]{baseCurrencyCode, targetCurrencyCode};
            }
        }
        throw new BadRequestException();
    }

    public static ExchangeRateRequestDto extractExchangeRateRequestDto(HttpServletRequest request) throws BadRequestException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        if (isValidCode(baseCurrencyCode) || isValidCode(targetCurrencyCode) || isValidRate(rate)) {
            return new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate));
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
