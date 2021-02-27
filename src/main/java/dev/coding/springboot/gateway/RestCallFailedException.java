package dev.coding.springboot.gateway;

public class RestCallFailedException extends RuntimeException {
    public RestCallFailedException(String message, Exception ex) {
    }
}
