package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.exception.NotFoundException;
import com.yoxaron.cuurency_exchange.service.ExchangeRateService;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.sendJsonResponse;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var requestDto = PathParser.extractCurrencyPairCodes(req);
        var rateDtoOptional = exchangeRateService.findByPairCode(requestDto);
        if (rateDtoOptional.isPresent()) {
            sendJsonResponse(resp, rateDtoOptional.get(), HttpServletResponse.SC_OK);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var requestDto = PathParser.extractExchangeRateUpdateRequestDto(req);
        sendJsonResponse(resp, exchangeRateService.update(requestDto), HttpServletResponse.SC_OK);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
