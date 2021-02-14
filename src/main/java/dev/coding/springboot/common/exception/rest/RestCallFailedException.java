package dev.coding.springboot.common.exception.rest;

import dev.coding.springboot.common.exception.SystemException;

public class RestCallFailedException extends SystemException {
    public RestCallFailedException(String message) {
        super(message);
    }

    public RestCallFailedException(Throwable cause) {
        super(cause);
    }

    public RestCallFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
