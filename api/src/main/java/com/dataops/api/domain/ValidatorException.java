package com.dataops.api.domain;

public class ValidatorException extends RuntimeException {
    public ValidatorException(String message) {
        super(message);
    }
}
