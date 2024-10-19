package com.yoxaron.cuurency_exchange.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorDetails errorDetails;

    public ApiException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }
}
