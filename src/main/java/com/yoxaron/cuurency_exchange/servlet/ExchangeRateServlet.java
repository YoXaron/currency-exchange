package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateDto;
import com.yoxaron.cuurency_exchange.exception.NotFoundException;
import com.yoxaron.cuurency_exchange.service.ExchangeRateService;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.sendJsonResponse;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] codes = PathParser.extractCurrencyPairCodes(req);
        Optional<ExchangeRateDto> rateDtoOptional = exchangeRateService.findByPairCode(codes);
        if (rateDtoOptional.isPresent()) {
            sendJsonResponse(resp, rateDtoOptional.get(), HttpServletResponse.SC_OK);
        } else {
            throw new NotFoundException();
        }

    }
}
