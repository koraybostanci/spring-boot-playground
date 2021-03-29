package dev.coding.springboot.gateway;

public class RestCallFailedException extends RuntimeException {
    public RestCallFailedException(String message) {
        super(message);
    }
    public RestCallFailedException(String message, Exception ex) {
        super(message, ex);
    }
}
