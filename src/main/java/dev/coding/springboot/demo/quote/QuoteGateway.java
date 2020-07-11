package dev.coding.springboot.demo.quote;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.gateway.RestGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class QuoteGateway extends RestGateway {

    private static final String PATH_KEY_QUOTE_RANDOM = "random";

    public QuoteGateway(final ServiceEndpointProperties serviceEndpointProperties, final RestTemplate restTemplate) {
        super(serviceEndpointProperties.getQuote(), restTemplate);
    }

    public Optional<Quote> getQuote() {
        final ResponseEntity<Quote> responseEntity = http.get(url.ofPath(PATH_KEY_QUOTE_RANDOM), Quote.class);
        return getValueOnSuccess(responseEntity);
    }
}
