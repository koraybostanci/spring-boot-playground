package dev.coding.springboot.gateway.httpbin;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.gateway.RestCallFailedException;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@ExtendWith(MockitoExtension.class)
public class HttpBinRestGatewayTest {

    private static final String HTTP_BIN_ENDPOINT_BASE_URL = "http://localhost:9000";
    private static final String HTTP_BIN_ENDPOINT_SLIDES_PATH_KEY = "get-slide-show-data";
    private static final String HTTP_BIN_ENDPOINT_SLIDES_PATH_VALUE = "/path";

    private HttpBinRestGateway httpBinRestGateway;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceEndpointProperties serviceEndpointProperties;

    @BeforeEach
    void onBeforeEach() {
        when(serviceEndpointProperties.getHttpBin()).thenReturn(getHttpBinServiceEndpoint());

        httpBinRestGateway = new HttpBinRestGateway(restTemplate, serviceEndpointProperties);
    }

    @Test
    public void getSlideShowData_throwsRestCallFailedException_whenRestCallThrowsRestClientException() {
        final RequestEntity requestEntity = requestEntityOfGetSlideShowData();
        when(restTemplate.exchange(requestEntity, SlideShowData.class)).thenThrow(new RestClientException("RestCallFailed"));

        assertThrows(RestCallFailedException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsRestCallFailedException_whenRestCallDoesNotReturnResponseEntity() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(null);

        assertThrows(RestCallFailedException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsRestCallFailedException_whenRestCallReturnsInternalServerError() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(new ResponseEntity<>(INTERNAL_SERVER_ERROR));

        assertThrows(RestCallFailedException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_throwsRestCallFailedException_whenRestCallReturnsNotFound() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(new ResponseEntity<>(NOT_FOUND));

        assertThrows(RestCallFailedException.class, () -> httpBinRestGateway.getSlideShowData());
    }

    @Test
    public void getSlideShowData_returnsSlideShowData_whenRestCallReturnsHttpOkWithData() {
        final SlideShowData anySlideShowData = anySlideShowData();

        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(ResponseEntity.ok(anySlideShowData));

        final SlideShowData retrievedSlideShowData = httpBinRestGateway.getSlideShowData().get();
        assertThat(retrievedSlideShowData).isEqualTo(anySlideShowData);
    }

    @Test
    public void getSlideShowData_returnsSlideShowData_whenRestCallReturnsHttpOkWithoutData() {
        when(restTemplate.exchange(requestEntityOfGetSlideShowData(), SlideShowData.class)).thenReturn(ResponseEntity.ok(null));

        final Optional<SlideShowData> retrievedSlideShowData = httpBinRestGateway.getSlideShowData();
        assertThat(retrievedSlideShowData).isEmpty();
    }

    private ServiceEndpointProperties.ServiceEndpoint getHttpBinServiceEndpoint() {
        final Map<String, String> paths = new HashMap<>();
        paths.put(HTTP_BIN_ENDPOINT_SLIDES_PATH_KEY, HTTP_BIN_ENDPOINT_SLIDES_PATH_VALUE);

        final ServiceEndpointProperties.ServiceEndpoint httpBinServiceEndpoint = new ServiceEndpointProperties.ServiceEndpoint();
        httpBinServiceEndpoint.setBaseUrl(HTTP_BIN_ENDPOINT_BASE_URL);
        httpBinServiceEndpoint.setPaths(paths);
        return httpBinServiceEndpoint;
    }

    private RequestEntity requestEntityOfGetSlideShowData() {
        return new RequestEntity(withHttpHeaders(), GET, uriOfGetSlideShowData());
    }

    private URI uriOfGetSlideShowData() {
        return fromHttpUrl(serviceEndpointProperties.getHttpBin().getBaseUrl())
                .path(serviceEndpointProperties.getHttpBin().getPath(HTTP_BIN_ENDPOINT_SLIDES_PATH_KEY))
                .build().toUri();
    }

    private HttpHeaders withHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON.getMimeType());
        return headers;
    }
}