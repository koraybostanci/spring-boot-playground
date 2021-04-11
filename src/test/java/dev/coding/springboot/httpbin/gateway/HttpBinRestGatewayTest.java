package dev.coding.springboot.httpbin.gateway;

import dev.coding.springboot.common.configuration.ServiceEndpointProperties;
import dev.coding.springboot.common.configuration.ServiceEndpointProperties.ServiceEndpoint;
import dev.coding.springboot.common.exception.SystemException;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@ExtendWith(MockitoExtension.class)
public class HttpBinRestGatewayTest {

    private static final String HTTP_BIN_SERVICE_ENDPOINT_BASE_URL = "http://localhost:9000";
    private static final String HTTP_BIN_SERVICE_ENDPOINT_SLIDES_PATH_KEY = "get-slide-show-data";
    private static final String HTTP_BIN_SERVICE_ENDPOINT_SLIDES_PATH_VALUE = "/path";

    private HttpBinRestGateway httpBinRestGateway;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceEndpointProperties serviceEndpointProperties;

    @BeforeEach
    void onBeforeEach() {
        when(serviceEndpointProperties.getHttpBinService()).thenReturn(getHttpBinServiceEndpoint());

        httpBinRestGateway = new HttpBinRestGateway(restTemplate, serviceEndpointProperties);
    }

    @Test
    public void getSlideShowData_throwsSystemException_whenRestCallThrowsRestClientException() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenThrow(new RestClientException("RestCallFailed"));

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsSystemException_whenRestCallDoesNotReturnResponseEntity() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(null);

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsSystemException_whenRestCallReturnsInternalServerError() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(new ResponseEntity<>(INTERNAL_SERVER_ERROR));

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsSystemException_whenRestCallReturnsNotFound() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(notFound().build());

        assertThrows(SystemException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_returnsSlideShowData_whenRestCallReturnsSuccessWithData() {
        final SlideShowData anySlideShowData = anySlideShowData();

        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(ok(anySlideShowData));

        final SlideShowData retrievedSlideShowData = httpBinRestGateway.getSlideShowData().get();
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    @Test
    public void getSlideShowData_returnsSlideShowData_whenRestCallReturnsSuccessWithoutData() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(ok(null));

        final Optional<SlideShowData> retrievedSlideShowData = httpBinRestGateway.getSlideShowData();
        assertThat(retrievedSlideShowData).isEmpty();
    }

    private ServiceEndpoint getHttpBinServiceEndpoint() {
        final Map<String, String> paths = new HashMap<>();
        paths.put(HTTP_BIN_SERVICE_ENDPOINT_SLIDES_PATH_KEY, HTTP_BIN_SERVICE_ENDPOINT_SLIDES_PATH_VALUE);

        final ServiceEndpoint httpBinServiceEndpoint = new ServiceEndpoint();
        httpBinServiceEndpoint.setBaseUrl(HTTP_BIN_SERVICE_ENDPOINT_BASE_URL);
        httpBinServiceEndpoint.setPaths(paths);
        return httpBinServiceEndpoint;
    }

    private RequestEntity requestEntityOfGetSlideShowData() {
        return new RequestEntity(withHttpHeaders(), GET, uriOfGetSlideShowData());
    }

    private URI uriOfGetSlideShowData() {
        return fromHttpUrl(serviceEndpointProperties.getHttpBinService().getBaseUrl())
                .path(serviceEndpointProperties.getHttpBinService().getPath(HTTP_BIN_SERVICE_ENDPOINT_SLIDES_PATH_KEY))
                .build().toUri();
    }

    private HttpHeaders withHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON.getMimeType());
        return headers;
    }
}