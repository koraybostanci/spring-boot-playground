package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class RestUrlBuilder {

    private final ServiceEndpoint serviceEndpoint;

    private RestUrlBuilder(ServiceEndpoint serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public static class Builder {
        public static RestUrlBuilder create(final ServiceEndpoint serviceEndpoint) {
            return new RestUrlBuilder(serviceEndpoint);
        }
    }

    public URI getBaseUri() {
        return getUriComponentsBuilder().build().toUri();
    }

    public URI ofPath(final String pathKey) {
        return getUriComponentsBuilder(pathKey).build().toUri();
    }

    public URI ofPath(final String pathKey, final Object... variables) {
        return getUriComponentsBuilder(pathKey).buildAndExpand(variables).toUri();
    }

    private UriComponentsBuilder getUriComponentsBuilder() {
        return fromHttpUrl(serviceEndpoint.getBaseUrl());
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String pathKey) {
        return getUriComponentsBuilder().path(serviceEndpoint.getPaths().get(pathKey));
    }
}
