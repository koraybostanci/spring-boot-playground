package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class GetRequestObject<TRequest, TResponse> {

    private URI uri;
    private TRequest requestBody;
    private final Class<TResponse> responseType;
    private final Map<String, String> customHeaders;
    private final ServiceEndpoint serviceEndpoint;

    public GetRequestObject(final ServiceEndpoint serviceEndpoint, final Class<TResponse> responseType) {
        this.serviceEndpoint = serviceEndpoint;
        this.responseType = responseType;
        this.customHeaders = new HashMap<>();
    }

    public void setPath(final String pathKey) {
        this.uri = serviceEndpoint.getPathUri(pathKey);
    }

    public void setPath(final String pathKey, final Object... variables) {
        this.uri = serviceEndpoint.getPathUri(pathKey, variables);
    }

    public URI getUri() {
        return uri;
    }

    public void setRequestBody(final TRequest requestBody) {
        this.requestBody = requestBody;
    }

    public Optional<TRequest> getRequestBody() {
        return ofNullable(requestBody);
    }

    public Class<TResponse> getResponseType() {
        return this.responseType;
    }

    public void setCustomHeaders(final Map<String, String> headers) {
        customHeaders.putAll(headers);
    }

    public Optional<Map<String, String>> getCustomHeaders() {
        return of(customHeaders);
    }
}
