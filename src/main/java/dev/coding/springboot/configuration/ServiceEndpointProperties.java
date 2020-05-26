package dev.coding.springboot.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Getter
@Setter
@Validated
@ConfigurationProperties("services")
public class ServiceEndpointProperties {

    private Service github;

    @Getter
    @Setter
    @Validated
    public static class Service {
        private String baseUrl;
        private Map<String, String> paths;

        public String getUrl(final String key) {
            return fromPath(baseUrl).path(getPath(key)).build().toUri().toString();
        }

        private String getPath(final String key) {
            return paths.get(key);
        }
    }
}
