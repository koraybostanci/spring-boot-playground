package dev.coding.springboot.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.coding.springboot.common.AbstractIntegrationTest;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        properties = "service-endpoints.http-bin.base-url: http://localhost:8888"
)
public class HttpBinControllerIT extends AbstractIntegrationTest {

    private final static WireMockServer wireMockServer = new WireMockServer(8888);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public static void onBeforeAll() {
        wireMockServer.start();
        configureFor("localhost", 8888);
    }

    @AfterAll
    public static void onAfterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void onBeforeEach() {
        wireMockServer.resetAll();

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getSlides_returnsSlideShowData_whenCallToHttpBinEndpointSucceeds() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(anySlideShowData);

        final MockHttpServletResponse response = performGetSlidesCall();
        final SlideShowData retrievedSlideShowData = objectMapper.readValue(response.getContentAsByteArray(), SlideShowData.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    private MockHttpServletResponse performGetSlidesCall() throws Exception {
         return mockMvc.perform(get("/slides"))
                 .andReturn()
                 .getResponse();
    }

    private void stubForGetSlidesOnSuccess(final SlideShowData anySlideShowData) throws JsonProcessingException {
        stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(OK.value())
                        .withBody(objectMapper.writeValueAsBytes(anySlideShowData))));
    }

}