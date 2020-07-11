package dev.coding.springboot.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    private static final String REST_CLIENT_ON_ERROR = "Rest call to [{}] [{}] received error due to [{}]";
    private static final String REST_CLIENT_ON_BEFORE_CALL = "Calling [{}] [{}]";
    private static final String REST_CLIENT_ON_AFTER_CALL = "Rest call to [{}] [{}] responded with [{}] [{}]";

    private final RestTemplate restTemplate;

    private RestClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class Builder {
        public static RestClient create(final RestTemplate restTemplate) {
            return new RestClient(restTemplate);
        }
    }

    public <T> ResponseEntity<T> get(final URI uri, final Class<T> responseType) {
        return exchange(GET, uri, null, empty(), responseType);
    }

    public <T> ResponseEntity<T> get(final URI uri, final Map<String, String> headers, final Class<T> responseType) {
        return exchange(GET, uri, null, of(headers), responseType);
    }

    public <T> ResponseEntity<T> post(final URI uri, final Object data, final Class<T> responseType) {
        return exchange(POST, uri, data, empty(), responseType);
    }

    public <T> ResponseEntity<T> post(final URI uri, final Object data, final Map<String, String> headers, final Class<T> responseType) {
        return exchange(POST, uri, data, of(headers), responseType);
    }

    public <T> ResponseEntity<T> put(final URI uri, final Object data, final Class<T> responseType) {
        return exchange(PUT, uri, data, empty(), responseType);
    }

    public <T> ResponseEntity<T> put(final URI uri, final Object data, final Map<String, String> headers, final Class<T> responseType) {
        return exchange(PUT, uri, data, of(headers), responseType);
    }

    private <T> ResponseEntity<T> exchange(final HttpMethod httpMethod, final URI uri, final Object data, final Optional<Map<String, String>> headers, final Class<T> responseType) {
        try {
            LOGGER.debug(REST_CLIENT_ON_BEFORE_CALL, httpMethod.name(), uri);
            final ResponseEntity<T> responseEntity = restTemplate.exchange(uri, httpMethod, httpEntityOf(data, headers), responseType);
            LOGGER.debug(REST_CLIENT_ON_AFTER_CALL, httpMethod.name(), uri, responseEntity.getStatusCode(), responseEntity.getBody());

            return responseEntity;
        } catch (final RestClientException ex) {
            LOGGER.error(REST_CLIENT_ON_ERROR, httpMethod.name(), uri, ex.toString());
        }

        return ResponseEntity.of(empty());
    }

    private HttpEntity httpEntityOf(final Object data, final Optional<Map<String, String>> customHeaders) {
        return new HttpEntity(data, buildHttpHeaders(customHeaders));
    }

    private HttpHeaders buildHttpHeaders(final Optional<Map<String, String>> customHeaders) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        if (customHeaders.isPresent()) {
            customHeaders.get().forEach(httpHeaders::set);
        }

        return httpHeaders;
    }
}
