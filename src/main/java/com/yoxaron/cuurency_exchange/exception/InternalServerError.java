package com.yoxaron.cuurency_exchange.exception;

public class InternalServerError extends ApiException {
    public InternalServerError() {
        super(ErrorDetails.INTERNAL_SERVER_ERROR);
    }
}
