package dev.coding.springboot.demo.github.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class TestRestGatewayHelper {

    public static HttpEntity buildHttpEntity() {
        return new HttpEntity(buildHttpHeaders());
    }

    public static HttpHeaders buildHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(singletonList(APPLICATION_JSON));

        return httpHeaders;
    }

}
