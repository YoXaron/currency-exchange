package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.service.CurrencyService;
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
            sendJsonResponse(resp, currencyService.findAll(), 200);
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }
}
