package dev.coding.springboot.demo.github;

import dev.coding.springboot.configuration.ServiceEndpoint;
import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.demo.github.domain.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException.BadGateway;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.coding.springboot.demo.github.common.TestObjectFactory.anyGithubUser;
import static dev.coding.springboot.demo.github.common.TestObjectFactory.anyGithubUserList;
import static dev.coding.springboot.demo.github.common.TestObjectFactory.ANY_GITHUB_USER_ID;
import static dev.coding.springboot.demo.github.common.TestObjectFactory.ANY_GITHUB_USER_NAME;
import static dev.coding.springboot.demo.github.common.TestRestGatewayHelper.buildHttpEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@ExtendWith(MockitoExtension.class)
public class GithubGatewayTest {

    private static final String BASE_URL = "http://github-service.remote/";
    private static final String PATH_KEY_USERS = "users";
    private static final String PATH_VALUE_USERS = "users";
    private static final String PATH_KEY_USER_BY_USERNAME = "user-by-username";
    private static final String PATH_VALUE_USER_BY_USERNAME = "users/{username}";

    @Mock
    private ServiceEndpointProperties serviceEndpointProperties;

    @Mock
    private RestTemplate restTemplate;

    private GithubGateway githubGateway;

    @BeforeEach
    public void setUp() {
        when(serviceEndpointProperties.getGithub()).thenReturn(buildGithubServiceEndpoint());
        githubGateway = new GithubGateway(serviceEndpointProperties, restTemplate);
    }

    @Test
    public void getUserByUsername_returnsRetrievedUser_whenUserRetrievedWithGivenUsername() {
        final GithubUser userToBeRetrieved = anyGithubUser(ANY_GITHUB_USER_ID, ANY_GITHUB_USER_NAME);

        when(restTemplate.exchange(buildGetUserByUsernameUri(ANY_GITHUB_USER_NAME), GET, buildHttpEntity(), GithubUser.class)).thenReturn(ok(userToBeRetrieved));
        final GithubUser userRetrieved = githubGateway.getUserByUsername(ANY_GITHUB_USER_NAME).get();

        assertThat(userRetrieved).isEqualTo(userToBeRetrieved);
    }

    @Test
    public void getUserByUsername_returnsEmpty_whenNotFoundExceptionReceived() {
        when(restTemplate.exchange(buildGetUserByUsernameUri(ANY_GITHUB_USER_NAME), GET, buildHttpEntity(), GithubUser.class))
                .thenThrow(NotFound.class);

        final Optional<GithubUser> userRetrieved = githubGateway.getUserByUsername(ANY_GITHUB_USER_NAME);

        assertThat(userRetrieved.isEmpty()).isTrue();
    }

    @Test
    public void getUserByUsername_returnsEmpty_whenBadGatewayExceptionReceived() {
        when(restTemplate.exchange(buildGetUserByUsernameUri(ANY_GITHUB_USER_NAME), GET, buildHttpEntity(), GithubUser.class))
                .thenThrow(BadGateway.class);

        final Optional<GithubUser> userRetrieved = githubGateway.getUserByUsername(ANY_GITHUB_USER_NAME);

        assertThat(userRetrieved.isEmpty()).isTrue();
    }

    @Test
    public void getUsers_returnsRetrievedUsers_whenUsersRetrieved() {
        final GithubUser[] usersToBeRetrieved = anyGithubUserList(3);

        when(restTemplate.exchange(buildGetUsersUri(), GET, buildHttpEntity(), GithubUser[].class))
                .thenReturn(ok(usersToBeRetrieved));
        final GithubUser[] usersRetrieved = githubGateway.getUsers().get();

        assertThat(usersRetrieved).isEqualTo(usersToBeRetrieved);
    }

    @Test
    public void getUsers_returnsEmpty_whenNotFoundExceptionReceived() {
        when(restTemplate.exchange(buildGetUsersUri(), GET, buildHttpEntity(), GithubUser[].class))
                .thenThrow(NotFound.class);

        final Optional<GithubUser[]> usersRetrieved = githubGateway.getUsers();

        assertThat(usersRetrieved.isEmpty()).isTrue();
    }

    @Test
    public void getUsers_returnsEmpty_whenBadGatewayExceptionReceived() {
        when(restTemplate.exchange(buildGetUsersUri(), GET, buildHttpEntity(), GithubUser[].class))
                .thenThrow(BadGateway.class);

        final Optional<GithubUser[]> usersRetrieved = githubGateway.getUsers();

        assertThat(usersRetrieved.isEmpty()).isTrue();
    }

    private ServiceEndpoint buildGithubServiceEndpoint() {
        final ServiceEndpoint serviceEndpoint = new ServiceEndpoint();
        serviceEndpoint.setBaseUrl(BASE_URL);

        final Map<String, String> paths = new HashMap<>();
        paths.put(PATH_KEY_USERS, PATH_VALUE_USERS);
        paths.put(PATH_KEY_USER_BY_USERNAME, PATH_VALUE_USER_BY_USERNAME);
        serviceEndpoint.setPaths(paths);

        return serviceEndpoint;
    }

    private URI buildGetUsersUri() {
        return fromHttpUrl(BASE_URL).path(PATH_VALUE_USERS).build().toUri();
    }

    private URI buildGetUserByUsernameUri(final String username) {
        return fromHttpUrl(BASE_URL).path(PATH_VALUE_USER_BY_USERNAME).buildAndExpand(username).toUri();
    }
}