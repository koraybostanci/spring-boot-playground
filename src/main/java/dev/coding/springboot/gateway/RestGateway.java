package dev.coding.springboot.gateway;

import dev.coding.springboot.common.exception.rest.RestCallFailedException;
import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
public abstract class RestGateway {

    private static final String REST_CALL_TO_URI = "Calling uri [{}] with requestEntity [{}]";
    private static final String REST_CALL_TO_URI_RESPONDED = "Rest call to uri [{}] responded with [{}]";
    private static final String REST_CALL_TO_URI_FAILED = "Rest call to uri [%s] failed with [%s]";

    private final RestTemplate restTemplate;
    private final ServiceEndpoint serviceEndpoint;

    public RestGateway(final RestTemplate restTemplate, final ServiceEndpoint serviceEndpoint) {
        this.restTemplate = restTemplate;
        this.serviceEndpoint = serviceEndpoint;
    }

    protected ServiceEndpoint getServiceEndpoint() {
        return serviceEndpoint;
    }

    public <T> Optional<T> httpGet(final URI uri, final Class<T> returnType) {
        final RequestEntity requestEntity = requestEntityOf(GET, uri);

        try {
            log.info(REST_CALL_TO_URI, uri, requestEntity);
            final ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, returnType);
            log.info(REST_CALL_TO_URI_RESPONDED, uri, responseEntity);

            return isSuccessfulResponse(responseEntity) ? ofNullable(responseEntity.getBody()) : empty();
        } catch (final RestClientException ex) {
            logAndRethrowException(uri, ex);
        }

        return empty();
    }

    private void logAndRethrowException(final URI uri, final Exception ex) {
        final String message = format(REST_CALL_TO_URI_FAILED, uri, ex);
        log.error(message);
        throw new RestCallFailedException(message, ex);
    }

    private RequestEntity requestEntityOf(final HttpMethod httpMethod, final URI uri) {
        return new RequestEntity(withHttpHeaders(), httpMethod, uri);
    }

    private HttpHeaders withHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON.getMimeType());
        return headers;
    }

    private boolean isSuccessfulResponse(final ResponseEntity responseEntity) {
        return responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful();
    }
}
