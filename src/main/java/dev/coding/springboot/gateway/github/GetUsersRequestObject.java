package dev.coding.springboot.gateway.github;

import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import dev.coding.springboot.gateway.GetRequestObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GetUsersRequestObject extends GetRequestObject<Void, GithubUser[]> {

    private static final String PATH_KEY_USERS = "users";

    public GetUsersRequestObject(final ServiceEndpoint serviceEndpoint) {
        super(serviceEndpoint, GithubUser[].class);
    }

    @Override
    public URI getUri() {
        return serviceEndpoint.getPathUri(PATH_KEY_USERS);
    }

    @Override
    public Optional<Map<String, String>> customHeaders() {
        final Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("key", "value");
        return Optional.of(customHeaders);
    }
}
