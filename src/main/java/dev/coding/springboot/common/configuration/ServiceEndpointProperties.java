package dev.coding.springboot.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Validated
@ConfigurationProperties("application.service-endpoints")
public class ServiceEndpointProperties {

    @Valid
    private final ServiceEndpoint httpBinService = new ServiceEndpoint();

    @Getter
    @Setter
    @Validated
    public static class ServiceEndpoint {

        @Valid
        private final ConnectionProperties connection = new ConnectionProperties();

        @NotBlank
        private String baseUrl;
        private Map<String, String> paths;

        public String getPath(final String key) {
            return paths.get(key);
        }
    }

    @Getter
    @Setter
    @Validated
    public static class ConnectionProperties {
        private int numberOfRoutes = 1;
        private int maxConnectionsPerRoute = 100;
        private int connectionRequestTimeout = 1000;
        private int connectTimeout = 1000;
        private int socketTimeout = 1000;
    }
}
