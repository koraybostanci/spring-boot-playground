package dev.coding.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfiguration {

    private static final int CONNECTION_REQUEST_TIMEOUT = 1_000;
    private static final int CONNECT_TIMEOUT = 1_000;
    private static final int READ_TIMEOUT = 1_000;

    @Bean
    @Primary
    public RestTemplate getDefaultRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory());
        return restTemplate;
    }
    
    private ClientHttpRequestFactory httpRequestFactory() {
        final HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        httpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT);
        return httpRequestFactory;
    }
}
