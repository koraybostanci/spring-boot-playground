package dev.coding.springboot.gateway.github;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.configuration.ServiceEndpoint;
import dev.coding.springboot.gateway.GetRequestObject;
import dev.coding.springboot.gateway.RestGateway;
import dev.coding.springboot.gateway.github.domain.GithubUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubRestGateway extends RestGateway {

    private static final String PATH_KEY_USERS = "users";
    private static final String PATH_KEY_USER_BY_USERNAME = "user-by-username";

    private final ServiceEndpoint serviceEndpoint;

    public GithubRestGateway(final ServiceEndpointProperties serviceEndpointProperties, final RestTemplate restTemplate) {
        super(restTemplate);
        this.serviceEndpoint = serviceEndpointProperties.getGithub();
    }

    public GithubUser[] getUsers() {
        final GetRequestObject requestObject = getUsersRequestObject();
        final ResponseEntity<GithubUser[]> responseEntity = getEntity(requestObject);
        return responseEntity.getBody();
    }

    public GithubUser getUserByUsername(final String username) {
        final GetRequestObject requestObject = getUserByUsernameRequestObject(username);
        final ResponseEntity<GithubUser> responseEntity = getEntity(requestObject);
        return responseEntity.getBody();
    }

    private GetRequestObject getUsersRequestObject() {
        final GetRequestObject requestObject = new GetRequestObject<Void, GithubUser[]>(serviceEndpoint, GithubUser[].class);
        requestObject.setPath(PATH_KEY_USERS);
        return requestObject;
    }

    private GetRequestObject getUserByUsernameRequestObject(final String username) {
        final GetRequestObject requestObject = new GetRequestObject<Void, GithubUser>(serviceEndpoint, GithubUser.class);
        requestObject.setPath(PATH_KEY_USER_BY_USERNAME, username);
        return requestObject;
    }

}
