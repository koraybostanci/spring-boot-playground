package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

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
        return fromPath(serviceEndpoint.getBaseUrl()).build().toUri();
    }

    public URI buildPathUri(final String pathKey) {
        return getUriComponentsBuilder(pathKey).build().toUri();
    }

    public URI buildPathUri(final String pathKey, final Object... variables) {
        return getUriComponentsBuilder(pathKey).buildAndExpand(variables).toUri();
    }

    public UriComponentsBuilder getUriComponentsBuilder(final String pathKey) {
        return fromHttpUrl(serviceEndpoint.getBaseUrl()).path(serviceEndpoint.getPaths().get(pathKey));
    }
}
