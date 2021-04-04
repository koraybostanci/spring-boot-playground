package dev.coding.springboot.httpbin.rest.controller;

import dev.coding.springboot.base.AbstractIntegrationTest;
import dev.coding.springboot.httpbin.gateway.HttpBinRestGateway;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
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

import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnSuccess;
import static dev.coding.springboot.common.TestObjectMapper.toObject;
import static dev.coding.springboot.common.configuration.CacheConfiguration.SLIDE_SHOW_DATA_CACHE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;
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
        stubForGetSlidesOnSuccess(getWireMockServer(), getObjectMapper(), anySlideShowData);

        final MockHttpServletResponse response = callGetSlidesEndpoint();
        final SlideShowData retrievedSlideShowData = toObject(getObjectMapper(), response.getContentAsByteArray(), SlideShowData.class);

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    @Test
    public void getSlides_returnsSlideShowDataFromCacheAndDoesNotCallGateway_whenCacheContainsData() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(getWireMockServer(), getObjectMapper(), anySlideShowData);

        callGetSlidesEndpoint();
        callGetSlidesEndpoint();

        verify(httpBinRestGateway, times(1)).getSlideShowData();
    }

    private MockHttpServletResponse callGetSlidesEndpoint() throws Exception {
         return mockMvc.perform(get("/slides"))
                 .andReturn()
                 .getResponse();
    }

}