package dev.coding.springboot.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Getter
@Setter
@Validated
@ConfigurationProperties("services")
public class ServiceEndpointProperties {

    private Service github = new Service();

    @Getter
    @Setter
    @Validated
    public static class Service {
        private String baseUrl;
        private Map<String, String> paths;

        public URI getBaseUri() {
            return fromPath(baseUrl).build().toUri();
        }

        public URI getPathUri(final String pathKey) {
            return fromPath(baseUrl).path(getPath(pathKey)).build().toUri();
        }

        private String getPath(final String key) {
            return paths.get(key);
        }
    }
}
