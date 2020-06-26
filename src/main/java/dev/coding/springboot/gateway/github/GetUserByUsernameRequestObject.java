package dev.coding.springboot.gateway.github;

import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import dev.coding.springboot.gateway.GetRequestObject;

import java.net.URI;

public class GetUserByUsernameRequestObject extends GetRequestObject<Void, GithubUser> {

    private static final String PATH_KEY_USER_BY_USERNAME = "user-by-username";
    private final String username;

    public GetUserByUsernameRequestObject(final ServiceEndpoint serviceEndpoint, final String username) {
        super(serviceEndpoint, GithubUser.class);
        this.username = username;
    }

    @Override
    public URI getUri() {
        return serviceEndpoint.getPathUri(PATH_KEY_USER_BY_USERNAME, username);
    }
}
