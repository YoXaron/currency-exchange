package com.yoxaron.cuurency_exchange.exception;

public class AlreadyExistsException extends ApiException {
    public AlreadyExistsException() {
        super(ErrorDetails.ALREADY_EXISTS);
    }
}
