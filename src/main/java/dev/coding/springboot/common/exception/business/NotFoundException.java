package dev.coding.springboot.common.exception.business;

import dev.coding.springboot.common.exception.BusinessException;

public class NotFoundException extends BusinessException {
    public NotFoundException(final String message) {
        super(message);
    }
    public NotFoundException(final Throwable cause) {
        super(cause);
    }
    public NotFoundException(final String message, final Throwable ex) {
        super(message, ex);
    }
}
