package com.yoxaron.cuurency_exchange.servlet;

import com.yoxaron.cuurency_exchange.dto.CurrencyDto;
import com.yoxaron.cuurency_exchange.exception.NotFoundException;
import com.yoxaron.cuurency_exchange.service.CurrencyService;
import com.yoxaron.cuurency_exchange.utils.PathParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static com.yoxaron.cuurency_exchange.utils.JsonResponseUtil.sendJsonResponse;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = PathParser.extractCurrencyCode(req);
        Optional<CurrencyDto> currencyOptional = currencyService.findByCode(code);
        if (currencyOptional.isPresent()) {
            sendJsonResponse(resp, currencyOptional.get(), HttpServletResponse.SC_OK);
        } else {
            throw new NotFoundException();
        }
    }
}
