package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.service.CurrencyService;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.sendJsonResponse;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            sendJsonResponse(resp, currencyService.findAll(), HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Currency currency = PathParser.extractCurrency(req);
            sendJsonResponse(resp, currencyService.save(currency), HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }
}
