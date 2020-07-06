package dev.coding.springboot.demo.github;

import static dev.coding.springboot.demo.github.common.TestObjectFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.demo.github.domain.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Optional;

@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GithubControllerIT {

    private static final String PATH_USERS = "/users";
    private static final String PATH_USER_BY_USERNAME = "/users/{username}";

    @Mock
    private RestTemplate restTemplate;

    @MockBean
    private ServiceEndpointProperties serviceEndpointProperties;

    @MockBean
    private GithubGateway githubGateway;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(context).build();
    }

    @Test
    public void getUserByUsername_returnsOkAndRetrievedUser_whenUserRetrieved() throws Exception {
        final GithubUser userToBeRetrieved = anyGithubUser(ANY_GITHUB_USER_ID, ANY_GITHUB_USER_NAME);
        when(githubGateway.getUserByUsername(ANY_GITHUB_USER_NAME)).thenReturn(Optional.of(userToBeRetrieved));

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_USER_BY_USERNAME, ANY_GITHUB_USER_NAME), OK);

        final GithubUser userRetrieved = objectMapper.readValue(response.getContentAsString(), GithubUser.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(userRetrieved).isEqualTo(userToBeRetrieved);
    }

    @Test
    public void getUserByUsername_returnsNotFound_whenNoUserRetrieved() throws Exception {
        when(githubGateway.getUserByUsername(ANY_GITHUB_USER_NAME)).thenReturn(Optional.empty());

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_USER_BY_USERNAME, ANY_GITHUB_USER_NAME), NOT_FOUND);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void getUsers_returnsOkAndRetrievedUsers_whenUsersRetrieved() throws Exception {
        final GithubUser[] usersToBeRetrieved = anyGithubUserList(3);
        when(githubGateway.getUsers()).thenReturn(Optional.of(usersToBeRetrieved));

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_USERS), OK);

        final GithubUser[] usersRetrieved = objectMapper.readValue(response.getContentAsString(), GithubUser[].class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(usersRetrieved).isEqualTo(usersToBeRetrieved);
    }

    @Test
    public void getUsers_returnsNotFound_whenNoUserRetrieved() throws Exception {
        when(githubGateway.getUsers()).thenReturn(Optional.empty());

        final MockHttpServletResponse response = performHttpGet(buildUri(PATH_USERS), NOT_FOUND);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    private URI buildUri(final String path) {
        return fromPath(path).build().toUri();
    }

    private URI buildUri(final String path, Object... values) {
        return fromPath(path).buildAndExpand(values).toUri();
    }

    private MockHttpServletResponse performHttpGet(final URI uri, final HttpStatus expectedHttpStatus) throws Exception {
        return mockMvc.perform(get(uri).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(expectedHttpStatus.value()))
                .andReturn()
                .getResponse();
    }
}
