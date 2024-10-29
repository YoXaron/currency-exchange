package com.yoxaron.cuurency_exchange.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoxaron.cuurency_exchange.dto.ErrorDto;
import com.yoxaron.cuurency_exchange.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> void sendJsonResponse(HttpServletResponse resp, T payload, Integer code) throws IOException {
        resp.setStatus(code);
        resp.getWriter().write(mapper.writeValueAsString(payload));
    }

    public static void sendErrorResponse(HttpServletResponse resp, ApiException e) throws IOException {
        var errorDetails = e.getErrorDetails();
        var errorDto = new ErrorDto(errorDetails.getMessage(), errorDetails.getCode());
        sendJsonResponse(resp, errorDto, errorDto.code());
    }
}
