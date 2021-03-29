package dev.coding.springboot.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import dev.coding.springboot.common.AbstractIntegrationTest;
import dev.coding.springboot.gateway.httpbin.HttpBinRestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static dev.coding.springboot.configuration.CacheConfiguration.SLIDE_SHOW_DATA_CACHE_NAME;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@SpringBootTest
@AutoConfigureMockMvc
public class HttpBinControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @SpyBean
    private HttpBinRestGateway httpBinRestGateway;

    @BeforeEach
    public void onBeforeEach() {
        getWireMockServer().resetAll();
        cacheManager.getCache(SLIDE_SHOW_DATA_CACHE_NAME).clear();
    }

    @Test
    public void getSlides_returnsSlideShowData_whenCallToHttpBinEndpointSucceeds() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(anySlideShowData);

        final MockHttpServletResponse response = callGetSlidesEndpoint();
        final SlideShowData retrievedSlideShowData = toObject(response.getContentAsByteArray(), SlideShowData.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    @Test
    public void getSlides_returnsSlideShowDataFromCacheAndDoesNotCallGateway_whenCacheContainsData() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(anySlideShowData);

        callGetSlidesEndpoint();
        callGetSlidesEndpoint();

        verify(httpBinRestGateway, times(1)).getSlideShowData();
    }

    private <T> T toObject(final byte[] contentAsByteArray, final Class<T> type) throws IOException {
        return getObjectMapper().readValue(contentAsByteArray, type);
    }

    private void stubForGetSlidesOnSuccess(final SlideShowData anySlideShowData) throws JsonProcessingException {
        getWireMockServer().stubFor(WireMock.get(urlPathEqualTo("/json"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(OK.value())
                        .withBody(getObjectMapper().writeValueAsBytes(anySlideShowData))));
    }

    private MockHttpServletResponse callGetSlidesEndpoint() throws Exception {
         return mockMvc.perform(get("/slides"))
                 .andReturn()
                 .getResponse();
    }

}