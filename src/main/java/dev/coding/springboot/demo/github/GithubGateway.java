package dev.coding.springboot.demo.github;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.demo.domain.User;
import dev.coding.springboot.gateway.RestGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
public class GithubGateway extends RestGateway {

    private static Logger LOGGER = LoggerFactory.getLogger(GithubGateway.class);

    private static final String PATH_KEY_USERS = "users";
    private static final String PATH_KEY_USER_BY_USERNAME = "user-by-username";

    public GithubGateway(final ServiceEndpointProperties serviceEndpointProperties, final RestTemplate restTemplate) {
        super(serviceEndpointProperties.getGithub(), restTemplate);
    }

    public Optional<User[]> getUsers() {
        final URI uri = buildPathUri(PATH_KEY_USERS);

        try {
            final ResponseEntity<User[]> responseEntity = get(uri, User[].class);
            return of(responseEntity.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOGGER.warn(format("Users can not be retrieved due to [%s]", ex.getMessage()));
        }

        return empty();
    }

    public Optional<User> getUserByUsername(final String username) {
        final URI uri = buildPathUri(PATH_KEY_USER_BY_USERNAME, username);

        try {
            final ResponseEntity<User> responseEntity = get(uri, User.class);
            return of(responseEntity.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOGGER.warn(format("User with username=[%s] can not be retrieved due to [%s]", username, ex.getMessage()));
        }
        return empty();
    }

}
