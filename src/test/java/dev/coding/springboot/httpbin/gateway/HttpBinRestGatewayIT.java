package dev.coding.springboot.httpbin.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import dev.coding.springboot.base.ContainerAwareIT;
import dev.coding.springboot.common.exception.SystemException;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnInternalServerError;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnNoResponseCode;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnNotFound;
import static dev.coding.springboot.common.TestStubFactory.stubForGetSlidesOnSuccess;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@SpringBootTest
public class HttpBinRestGatewayIT extends ContainerAwareIT {

    private static final int WIREMOCK_PORT = 8001;
    private static final WireMockServer wireMockServer = new WireMockServer(WIREMOCK_PORT);

    @Autowired
    private HttpBinRestGateway httpBinRestGateway;

    @SpyBean
    private RestTemplate restTemplate;

    @BeforeAll
    public static void onBeforeAll() {
        wireMockServer.start();
    }

    @AfterAll
    public static void onAfterAll() {
        wireMockServer.stop();
    }

    @Test
    public void getSlideShowData_returnsData_whenGatewaySucceeds() throws JsonProcessingException {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(wireMockServer, anySlideShowData);

        final SlideShowData retrievedSlideShowData = httpBinRestGateway.getSlideShowData().get();

        assertThat(anySlideShowData).isEqualTo(retrievedSlideShowData);
        verify(restTemplate, times(1)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallDoesNotReturnResponseEntity() throws JsonProcessingException {
        stubForGetSlidesOnNoResponseCode(wireMockServer);

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallReturnsInternalServerError() {
        stubForGetSlidesOnInternalServerError(wireMockServer);

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallReturnsNotFound() {
        stubForGetSlidesOnNotFound(wireMockServer);

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }
}
