package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.http.HttpMethod.GET;

public abstract class RestGateway {

    private final ServiceEndpoint serviceEndpoint;
    private final RestTemplate restTemplate;

    public RestGateway(final ServiceEndpoint serviceEndpoint, final RestTemplate restTemplate) {
        this.serviceEndpoint = serviceEndpoint;
        this.restTemplate = restTemplate;
    }

    protected <T> ResponseEntity<T> getEntity(final RequestEntity<?> requestEntity, final Class<T> responseType) {
        return getEntity(serviceEndpoint.getBaseUri(), requestEntity, responseType);
    }

    protected <T> ResponseEntity<T> getEntity(final String pathKey, final RequestEntity<?> requestEntity, final Class<T> responseType) {
        return getEntity(serviceEndpoint.getPathUri(pathKey), requestEntity, responseType);
    }

    protected <T> ResponseEntity<T> getEntity(final URI uri, final RequestEntity<?> requestEntity, final Class<T> responseType) {
        return restTemplate.exchange(uri, GET, requestEntity, responseType);
    }

}
