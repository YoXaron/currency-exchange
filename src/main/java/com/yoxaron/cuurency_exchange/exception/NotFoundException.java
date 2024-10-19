package com.yoxaron.cuurency_exchange.exception;

public class NotFoundException extends ApiException {
    public NotFoundException() {
        super(ErrorDetails.RESOURCE_NOT_FOUND);
    }
}
