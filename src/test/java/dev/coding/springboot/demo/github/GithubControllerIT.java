package dev.coding.springboot.demo.github;

import static dev.coding.springboot.demo.github.common.TestObjectFactory.anyGithubUserList;
import static dev.coding.springboot.demo.github.common.TestRestGatewayHelper.buildHttpEntity;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.ServiceEndpoint;
import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.demo.github.domain.GithubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class GithubControllerIT {

    private static final String BASE_URL = "http://github-service.local/";
    private static final String PATH_USERS = "users";
    private static final String PATH_USER_BY_USERNAME = "users/{username}";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceEndpointProperties serviceEndpointProperties;

    @Mock
    private GithubGateway githubGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        when(serviceEndpointProperties.getGithub()).thenReturn(buildGithubServiceEndpoint());
        githubGateway = new GithubGateway(serviceEndpointProperties, restTemplate);
    }

    @Test
    public void getUsers() throws Exception {
        final GithubUser[] usersToBeRetrieved = anyGithubUserList(3);

        when(restTemplate.exchange(buildGetUsersUri(), HttpMethod.GET, buildHttpEntity(), GithubUser[].class))
                .thenReturn(ok(usersToBeRetrieved));

        final MvcResult mvcResult = mockMvc.perform(get(BASE_URL + PATH_USERS))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final String contentAsString = mvcResult.getResponse().getContentAsString();
    }

    private URI buildGetUsersUri() {
        return fromHttpUrl(BASE_URL).path(PATH_USERS).build().toUri();
    }

    private URI buildGetUserByUsernameUri(final String username) {
        return fromHttpUrl(BASE_URL).path(PATH_USER_BY_USERNAME).buildAndExpand(username).toUri();
    }

    private ServiceEndpoint buildGithubServiceEndpoint() {
        final ServiceEndpoint serviceEndpoint = new ServiceEndpoint();
        serviceEndpoint.setBaseUrl("http://github-gateway.remote");

        final Map<String, String> paths = new HashMap<>();
        paths.put("users", "users");
        paths.put("user-by-username", "users/{username}");
        serviceEndpoint.setPaths(paths);

        return serviceEndpoint;
    }
}
