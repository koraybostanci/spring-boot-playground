package dev.coding.springboot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@Getter
@Setter
@Validated
public class ServiceEndpoint {

    private String baseUrl;
    private Map<String, String> paths;

    public URI getBaseUri() {
        return fromPath(baseUrl).build().toUri();
    }

    public URI getPathUri(final String pathKey) {
        return getUriComponentsBuilder(pathKey).build().toUri();
    }

    public URI getPathUri(final String pathKey, final Object... variables) {
        return getUriComponentsBuilder(pathKey).buildAndExpand(variables).toUri();
    }

    private UriComponentsBuilder getUriComponentsBuilder(final String pathKey) {
        return fromHttpUrl(baseUrl).path(paths.get(pathKey));
    }
}
