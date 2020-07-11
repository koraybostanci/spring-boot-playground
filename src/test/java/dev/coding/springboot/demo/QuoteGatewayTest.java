package dev.coding.springboot.demo;

import dev.coding.springboot.configuration.ServiceEndpoint;
import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.demo.quote.Quote;
import dev.coding.springboot.demo.quote.QuoteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException.BadGateway;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.coding.springboot.demo.common.TestObjectFactory.anyQuote;
import static dev.coding.springboot.demo.common.TestObjectFactory.buildHttpEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@ExtendWith(MockitoExtension.class)
public class QuoteGatewayTest {

    private static final String BASE_URL = "http://quote-service.remote/";
    private static final String PATH_KEY_QUOTE = "random";
    private static final String PATH_VALUE_QUOTE = "random";

    @Mock
    private RestTemplate restTemplate;

    private QuoteGateway quoteGateway;

    @BeforeEach
    public void setUp() {
        final ServiceEndpointProperties serviceEndpointProperties = anyServiceEndpointProperties();
        quoteGateway = new QuoteGateway(serviceEndpointProperties, restTemplate);
    }

    @Test
    public void getQuote_returnsRetrievedQuote_whenQuoteRetrieved() {
        final Quote expectedQuote = anyQuote();

        when(restTemplate.exchange(buildGetQuoteUri(), GET, buildHttpEntity(), Quote.class)).thenReturn(ok(expectedQuote));
        final Quote retrievedQuote = quoteGateway.getQuote().get();

        assertThat(retrievedQuote).isEqualTo(expectedQuote);
    }

    @Test
    public void getQuote_returnsEmpty_whenNotFoundExceptionReceived() {
    when(restTemplate.exchange(buildGetQuoteUri(), GET, buildHttpEntity(), Quote.class))
        .thenThrow(HttpClientErrorException.NotFound.class);

        final Optional<Quote> quoteRetrieved = quoteGateway.getQuote();

        assertThat(quoteRetrieved.isEmpty()).isTrue();
    }

    @Test
    public void getQuote_returnsEmpty_whenBadGatewayExceptionReceived() {
        when(restTemplate.exchange(buildGetQuoteUri(), GET, buildHttpEntity(), Quote.class))
                .thenThrow(BadGateway.class);

        final Optional<Quote> quoteRetrieved = quoteGateway.getQuote();

        assertThat(quoteRetrieved.isEmpty()).isTrue();
    }

    private ServiceEndpointProperties anyServiceEndpointProperties() {
        final ServiceEndpointProperties serviceEndpointProperties = new ServiceEndpointProperties();

        final ServiceEndpoint serviceEndpoint = serviceEndpointProperties.getQuote();
        serviceEndpoint.setBaseUrl(BASE_URL);

        final Map<String, String> paths = new HashMap<>();
        paths.put(PATH_KEY_QUOTE, PATH_VALUE_QUOTE);
        serviceEndpoint.setPaths(paths);

        return serviceEndpointProperties;
    }

    private URI buildGetQuoteUri() {
        return fromHttpUrl(BASE_URL).path(PATH_VALUE_QUOTE).build().toUri();
    }

}