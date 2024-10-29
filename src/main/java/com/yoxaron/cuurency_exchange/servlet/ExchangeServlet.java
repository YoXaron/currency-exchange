package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.exception.NotFoundException;
import com.yoxaron.cuurency_exchange.service.ExchangeService;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.sendJsonResponse;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            var requestDto = PathParser.extractExchangeRequestDto(req);
            var exchangeDtoOptional = exchangeService.exchange(requestDto);
            if (exchangeDtoOptional.isPresent()) {
                sendJsonResponse(resp, exchangeDtoOptional.get(), HttpServletResponse.SC_OK);
            } else {
                throw new NotFoundException();
            }
        } catch (IOException e) {
            throw new InternalServerError();
        }
    }
}
