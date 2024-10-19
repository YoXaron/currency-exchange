package com.yoxaron.cuurency_exchange.exception;

public class BadRequestException extends ApiException {
    public BadRequestException() {
        super(ErrorDetails.BAD_REQUEST);
    }
}
