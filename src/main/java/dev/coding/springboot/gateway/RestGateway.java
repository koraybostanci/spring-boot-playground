package dev.coding.springboot.gateway;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public abstract class RestGateway {

    private final RestTemplate restTemplate;

    public RestGateway(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> ResponseEntity<T> getEntity(final GetRequestObject requestObject) {
        return restTemplate.exchange(requestObject.getUri(), GET, buildHttpEntity(requestObject), requestObject.getResponseType());
    }

    private HttpHeaders buildHttpHeaders(final GetRequestObject requestObject) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        final Optional<Map<String, String>> customHeaders = requestObject.getCustomHeaders();

        if (customHeaders.isPresent()) {
            customHeaders.get().entrySet().stream()
                    .forEach(entry -> httpHeaders.add(entry.getKey(), entry.getValue()));
        }

        return httpHeaders;
    }

    private <T> HttpEntity<T> buildHttpEntity(final GetRequestObject requestObject) {
        final HttpHeaders httpHeaders = buildHttpHeaders(requestObject);
        return requestObject.getRequestBody().isPresent()
            ? new HttpEntity(requestObject.getRequestBody().get(), httpHeaders)
            : new HttpEntity(httpHeaders);
    }

}
