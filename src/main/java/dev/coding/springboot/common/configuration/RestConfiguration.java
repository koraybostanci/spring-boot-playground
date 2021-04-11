package dev.coding.springboot.common.configuration;

import org.apache.http.client.config.RequestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.apache.http.impl.client.HttpClientBuilder.create;

@Configuration
public class RestConfiguration {

    public static final String HTTP_BIN_SERVICE_REST_TEMPLATE = "httpBinServiceRestTemplate";

    @Bean(HTTP_BIN_SERVICE_REST_TEMPLATE)
    public RestTemplate getHttpBinServiceRestTemplate(final ServiceEndpointProperties properties) {
        return getRestTemplate(properties.getHttpBinService());
    }

    private RestTemplate getRestTemplate(final ServiceEndpointProperties.ServiceEndpoint endpoint) {
        return new RestTemplate(clientHttpRequestFactory(endpoint));
    }

    public ClientHttpRequestFactory clientHttpRequestFactory(final ServiceEndpointProperties.ServiceEndpoint endpoint) {
        final ServiceEndpointProperties.ConnectionProperties connection = endpoint.getConnection();
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connection.getConnectionRequestTimeout())
                .setConnectTimeout(connection.getConnectTimeout())
                .setSocketTimeout(connection.getSocketTimeout())
                .build();

        final HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(create()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnPerRoute(connection.getMaxConnectionsPerRoute())
                .setMaxConnTotal(connection.getMaxConnectionsPerRoute() * connection.getNumberOfRoutes())
                .build());

        return httpRequestFactory;
    }

}
