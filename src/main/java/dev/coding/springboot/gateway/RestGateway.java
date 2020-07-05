package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public abstract class RestGateway {

    private final RestTemplate restTemplate;
    private final ServiceEndpoint serviceEndpoint;

    public RestGateway(final ServiceEndpoint serviceEndpoint, final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.serviceEndpoint = serviceEndpoint;
    }

    protected <T> ResponseEntity<T> get(final URI uri, final Class<T> responseType) {
        return get(uri, responseType, null);
    }

    protected <T> ResponseEntity<T> get(final URI uri, final Class<T> responseType, final Map<String, String> customHeaders) {
        return restTemplate.exchange(uri, GET, buildHttpEntity(ofNullable(customHeaders)), responseType);
    }

    protected <T> ResponseEntity<T> post(final URI uri, final Object data, final Class<T> responseType) {
        return post(uri, data, responseType, null);
    }

    protected <T> ResponseEntity<T> post(final URI uri, final Object data, final Class<T> responseType, final Map<String, String> customHeaders) {
        return restTemplate.exchange(uri, POST, buildHttpEntity(data, ofNullable(customHeaders)), responseType);
    }

    protected URI getBaseUri() {
        return fromPath(serviceEndpoint.getBaseUrl()).build().toUri();
    }

    protected URI buildPathUri(final String pathKey) {
        return getUriComponentsBuilder(pathKey).build().toUri();
    }

    protected URI buildPathUri(final String pathKey, final Object... variables) {
        return getUriComponentsBuilder(pathKey).buildAndExpand(variables).toUri();
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String pathKey) {
        return fromHttpUrl(serviceEndpoint.getBaseUrl()).path(serviceEndpoint.getPaths().get(pathKey));
    }

    private HttpEntity buildHttpEntity(final Optional<Map<String, String>> customHeaders) {
        return new HttpEntity(buildHttpHeaders(customHeaders));
    }

    private HttpEntity buildHttpEntity(final Object data, final Optional<Map<String, String>> customHeaders) {
        return new HttpEntity(data, buildHttpHeaders(customHeaders));
    }

    private HttpHeaders buildHttpHeaders(final Optional<Map<String, String>> customHeaders) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        if (customHeaders.isPresent()) {
            customHeaders.get().entrySet().stream()
                    .forEach(entry -> httpHeaders.add(entry.getKey(), entry.getValue()));
        }

        return httpHeaders;
    }

}
