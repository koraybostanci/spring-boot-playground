package dev.coding.springboot.demo.common;

import dev.coding.springboot.demo.quote.Quote;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.net.URI;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

public class TestObjectFactory {

    public static Quote anyQuote() {
        final Quote quote = new Quote();
        quote.setType("anyQuoteType");
        return quote;
    }

    public static HttpEntity buildHttpEntity() {
        return new HttpEntity(buildHttpHeaders());
    }

    public static HttpHeaders buildHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        return httpHeaders;
    }

    public static URI buildUri(final String path) {
        return fromPath(path).build().toUri();
    }
}
