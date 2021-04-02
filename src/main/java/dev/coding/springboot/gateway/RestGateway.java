package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import dev.coding.springboot.common.exception.SystemException;
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
import static java.util.Optional.ofNullable;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
public abstract class RestGateway {

    private static final String REST_CALL_TO_URI = "Calling uri [{}] with requestEntity [{}]";
    private static final String REST_CALL_TO_URI_RESPONDED = "Rest call to uri [{}] responded with [{}]";
    private static final String REST_CALL_TO_URI_FAILED = "Rest call to uri [%s] failed with reason: [%s]";

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

        ResponseEntity<T> responseEntity;
        try {
            log.debug(REST_CALL_TO_URI, uri, requestEntity);
            responseEntity = restTemplate.exchange(requestEntity, returnType);
            log.info(REST_CALL_TO_URI_RESPONDED, uri, responseEntity);
        } catch (final RestClientException ex) {
            throw new SystemException(format(REST_CALL_TO_URI_FAILED, uri, ex), ex);
        }

        return processResponseEntity(uri, responseEntity);
    }

    private <T> Optional<T> processResponseEntity(final URI uri, final ResponseEntity<T> responseEntity) {
        if (responseEntity == null) {
            throw new SystemException(format(REST_CALL_TO_URI_FAILED, uri, "Null responseEntity"));
        }

        if (!isSuccessfulResponse(responseEntity)) {
            throw new SystemException(format(REST_CALL_TO_URI_FAILED, uri, format("Unsuccessful responseCode: %s", responseEntity.getStatusCode())));
        }

        return ofNullable(responseEntity.getBody());
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
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
