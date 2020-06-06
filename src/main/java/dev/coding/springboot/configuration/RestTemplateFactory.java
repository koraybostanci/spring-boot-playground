package dev.coding.springboot.configuration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RestTemplateFactory {

    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 1000;

    private RestTemplateFactory() {
    }

    public static RestTemplate getBasicRestTemplate() {
        final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(UTF_8.name())));
        return restTemplate;
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return requestFactory;
    }
}
