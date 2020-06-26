package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;

public abstract class GetRequestObject<TRequest, TResponse> {

    private Class<TResponse> responseType;

    protected final ServiceEndpoint serviceEndpoint;

    public GetRequestObject(final ServiceEndpoint serviceEndpoint, final Class<TResponse> responseType) {
        this.serviceEndpoint = serviceEndpoint;
        this.responseType = responseType;
    }

    public abstract URI getUri();

    public Optional<TRequest> getRequestBody() {
        return empty();
    }

    public Class<TResponse> getResponseType() {
        return responseType;
    }

    public Optional<Map<String, String>> customHeaders() {
        return empty();
    }
}
