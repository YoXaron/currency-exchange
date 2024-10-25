package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateRequestDto;
import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.service.ExchangeRateService;
import com.yoxaron.cuurency_exchange.utils.JsonResponseUtil;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            JsonResponseUtil.sendJsonResponse(resp, exchangeRateService.findAll(), HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ExchangeRateRequestDto requestDto = PathParser.extractExchangeRateRequestDto(req);
            JsonResponseUtil.sendJsonResponse(resp, exchangeRateService.save(requestDto), HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }
}
