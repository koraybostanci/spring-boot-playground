package dev.coding.springboot.common.exception;

public class SystemException extends Throwable {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
