package dev.coding.springboot.gateway;


import org.springframework.core.NestedRuntimeException;

public class RestGatewayException extends NestedRuntimeException {

    public RestGatewayException(String msg) {
        super(msg);
    }

    public RestGatewayException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
