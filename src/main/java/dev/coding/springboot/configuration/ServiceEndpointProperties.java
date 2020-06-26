package dev.coding.springboot.configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Getter
@Setter
@Validated
@ConfigurationProperties("service-endpoints")
public class ServiceEndpointProperties {

    private ServiceEndpoint github = new ServiceEndpoint();

    public ServiceEndpoint getGithub() {
        return github;
    }

    @Getter
    @Setter
    @Validated
    public static class ServiceEndpoint {
        private String baseUrl;
        private Map<String, String> paths;

        public URI getBaseUri() {
            return fromPath(baseUrl).build().toUri();
        }

        public URI getPathUri(final String pathKey) {
            return fromHttpUrl(baseUrl).path(getPath(pathKey)).build().toUri();
        }

        public URI getPathUri(final String pathKey, final Object... variables) {
            return fromHttpUrl(baseUrl).path(getPath(pathKey)).buildAndExpand(variables).toUri();
        }

        private String getPath(final String key) {
            return paths.get(key);
        }
    }
}
