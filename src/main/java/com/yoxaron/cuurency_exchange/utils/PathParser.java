package com.yoxaron.cuurency_exchange.utils;

import com.yoxaron.cuurency_exchange.exception.BadRequestException;
import com.yoxaron.cuurency_exchange.model.Currency;
import jakarta.servlet.http.HttpServletRequest;

public class PathParser {

    public static String extractCurrencyCode(HttpServletRequest request) throws BadRequestException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 4 && pathInfo.startsWith("/")) {
            String currencyCode = pathInfo.substring(1).toUpperCase();
            if (validateCode(currencyCode)) {
                return currencyCode;
            }
        }
        throw new BadRequestException();
    }

    public static Currency extractCurrency(HttpServletRequest request) throws BadRequestException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (name == null || name.isBlank() ||
            code == null || !validateCode(code) ||
            sign == null || sign.isBlank()) {
            throw new BadRequestException();
        }

        Currency currency = new Currency();
        currency.setFullName(name);
        currency.setCode(code.toUpperCase());
        currency.setSign(sign);

        return currency;
    }

    public static String[] extractCurrencyPairCodes(HttpServletRequest request) throws BadRequestException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 7 && pathInfo.startsWith("/")) {
            String baseCurrencyCode = pathInfo.substring(1, 4).toUpperCase();
            String targetCurrencyCode = pathInfo.substring(4).toUpperCase();

            if (validateCode(baseCurrencyCode) && validateCode(targetCurrencyCode)) {
                return new String[] { baseCurrencyCode, targetCurrencyCode };
            }
        }
        throw new BadRequestException();
    }

    private static boolean validateCode(String code) {
        return code.toUpperCase().matches("[A-Z]{3}");
    }
}
