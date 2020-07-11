package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;
import dev.coding.springboot.demo.quote.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public abstract class RestGateway {

    protected final RestClient http;
    protected final RestUrlBuilder url;

    public RestGateway(final ServiceEndpoint serviceEndpoint, final RestTemplate restTemplate) {
        this.http = RestClient.Builder.create(restTemplate);
        this.url = RestUrlBuilder.Builder.create(serviceEndpoint);
    }

    protected Optional<Quote> getValueOnSuccess(final ResponseEntity<Quote> responseEntity) {
        return responseEntity.getStatusCode().is2xxSuccessful() ? of(responseEntity.getBody()) : empty();
    }

}
