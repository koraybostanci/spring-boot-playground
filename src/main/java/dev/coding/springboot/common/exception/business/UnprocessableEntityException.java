package dev.coding.springboot.common.exception.business;

import dev.coding.springboot.common.exception.BusinessException;

public class UnprocessableEntityException extends BusinessException {
    public UnprocessableEntityException(final String message) {
        super(message);
    }
    public UnprocessableEntityException(final Throwable cause) {
        super(cause);
    }
    public UnprocessableEntityException(final String message, final Throwable ex) {
        super(message, ex);
    }
}
