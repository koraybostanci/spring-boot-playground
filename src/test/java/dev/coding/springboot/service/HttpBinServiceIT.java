package dev.coding.springboot.service;

import dev.coding.springboot.common.AbstractIntegrationTest;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import dev.coding.springboot.service.httpbin.HttpBinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnSuccess;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpBinServiceIT extends AbstractIntegrationTest {

    @Autowired
    private HttpBinService httpBinService;

    @BeforeEach
    public void onBeforeEach() {
        wireMockServer.resetAll();
    }

    @Test
    public void getSlides_returnsSlideShowData_whenCallToHttpBinEndpointSucceeds() throws Exception {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(anySlideShowData);

        final SlideShowData retrievedSlideShowData = httpBinService.getSlideShowData().get();
        assertThat(anySlideShowData).isEqualTo(retrievedSlideShowData);
    }

}
