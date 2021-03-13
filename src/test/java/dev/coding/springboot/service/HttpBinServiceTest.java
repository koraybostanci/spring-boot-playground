package dev.coding.springboot.service;

import dev.coding.springboot.gateway.httpbin.HttpBinRestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import dev.coding.springboot.service.httpbin.HttpBinService;
import dev.coding.springboot.service.httpbin.HttpBinServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.coding.springboot.common.TestObjectFactory.anySlideShowData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpBinServiceTest {

    @Mock
    private HttpBinRestGateway httpBinRestGateway;

    private HttpBinService httpBinService;

    @BeforeEach
    public void beforeEach() {
        httpBinService = new HttpBinServiceImpl(httpBinRestGateway);
    }

    @Test
    public void getSlideShowData_returnsSlideShowData_whenHttpBinEndpointSucceeds() {
        final SlideShowData slideShowData = anySlideShowData();
        when(httpBinRestGateway.getSlideShowData()).thenReturn(Optional.of(slideShowData));

        final Optional<SlideShowData> retrievedSlideShowData = httpBinService.getSlideShowData();

        assertThat(retrievedSlideShowData.get()).isEqualTo(slideShowData);
    }

    @Test
    public void getSlideShowData_doesNotReturnSlideShowData_whenHttpBinEndpointReturnsEmpty() {
        when(httpBinRestGateway.getSlideShowData()).thenReturn(Optional.empty());

        final Optional<SlideShowData> retrievedSlideShowData = httpBinService.getSlideShowData();

        assertThat(retrievedSlideShowData).isEmpty();
    }
}