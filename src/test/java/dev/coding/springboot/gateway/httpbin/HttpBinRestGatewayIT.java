package dev.coding.springboot.gateway.httpbin;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.coding.springboot.base.AbstractIntegrationTest;
import dev.coding.springboot.common.exception.SystemException;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
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
public class HttpBinRestGatewayIT extends AbstractIntegrationTest {

    @Autowired
    private HttpBinRestGateway httpBinRestGateway;

    @SpyBean
    private RestTemplate restTemplate;

    @Test
    public void getSlideShowData_returnsData_whenGatewaySucceeds() throws JsonProcessingException {
        final SlideShowData anySlideShowData = anySlideShowData();
        stubForGetSlidesOnSuccess(getWireMockServer(), getObjectMapper(), anySlideShowData);

        final SlideShowData retrievedSlideShowData = httpBinRestGateway.getSlideShowData().get();

        assertThat(anySlideShowData).isEqualTo(retrievedSlideShowData);
        verify(restTemplate, times(1)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallDoesNotReturnResponseEntity() throws JsonProcessingException {
        stubForGetSlidesOnNoResponseCode(getWireMockServer(), getObjectMapper());

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallReturnsInternalServerError() {
        stubForGetSlidesOnInternalServerError(getWireMockServer());

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }

    @Test
    public void getSlideShowData_retriesForConfiguredAttemptCount_whenRestCallReturnsNotFound() {
        stubForGetSlidesOnNotFound(getWireMockServer());

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
        verify(restTemplate, times(3)).exchange(any(RequestEntity.class), eq(SlideShowData.class));
    }
}
