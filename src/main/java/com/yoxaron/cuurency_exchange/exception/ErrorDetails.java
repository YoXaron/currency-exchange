package com.yoxaron.cuurency_exchange.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public enum ErrorDetails {

    RESOURCE_NOT_FOUND("Resource not found", HttpServletResponse.SC_NOT_FOUND),
    BAD_REQUEST("Bad request", HttpServletResponse.SC_BAD_REQUEST),
    INTERNAL_SERVER_ERROR("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    private final String message;
    private final int code;

    ErrorDetails(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
