package dev.coding.springboot.common.exception.business;

import dev.coding.springboot.common.exception.BusinessException;

public class ConflictException extends BusinessException {
    public ConflictException(final String message) {
        super(message);
    }
    public ConflictException(final Throwable cause) {
        super(cause);
    }
    public ConflictException(final String message, final Throwable ex) {
        super(message, ex);
    }
}
