package dev.coding.springboot.gateway;

import dev.coding.springboot.configuration.ServiceEndpoint;
import org.springframework.web.client.RestTemplate;

public abstract class RestGateway {

    protected final RestClient http;
    protected final RestUrlBuilder url;

    public RestGateway(final ServiceEndpoint serviceEndpoint, final RestTemplate restTemplate) {
        this.http = RestClient.Builder.create(restTemplate);
        this.url = RestUrlBuilder.Builder.create(serviceEndpoint);
    }

}
