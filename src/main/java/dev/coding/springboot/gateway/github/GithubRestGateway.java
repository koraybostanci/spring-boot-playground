package dev.coding.springboot.gateway.github;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.configuration.ServiceEndpointProperties.ServiceEndpoint;
import dev.coding.springboot.gateway.GetRequestObject;
import dev.coding.springboot.gateway.RestGateway;
import dev.coding.springboot.gateway.github.domain.GithubUser;
import dev.coding.springboot.gateway.github.request.GetUserByUsernameRequestObject;
import dev.coding.springboot.gateway.github.request.GetUsersRequestObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubRestGateway extends RestGateway {

    private final ServiceEndpoint serviceEndpoint;

    public GithubRestGateway(final ServiceEndpointProperties serviceEndpointProperties, final RestTemplate restTemplate) {
        super(restTemplate);
        this.serviceEndpoint = serviceEndpointProperties.getGithub();
    }

    public GithubUser[] getUsers() {
        final GetRequestObject requestObject = new GetUsersRequestObject(serviceEndpoint);
        final ResponseEntity<GithubUser[]> responseEntity = getEntity(requestObject);
        return responseEntity.getBody();
    }

    public GithubUser getUserByUsername(final String username) {
        final GetRequestObject requestObject = new GetUserByUsernameRequestObject(serviceEndpoint, username);
        final ResponseEntity<GithubUser> responseEntity = getEntity(requestObject);
        return responseEntity.getBody();
    }

}
