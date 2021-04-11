package dev.coding.springboot.httpbin.gateway;

import dev.coding.springboot.common.configuration.ServiceEndpointProperties;
import dev.coding.springboot.common.gateway.RestGateway;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Component
@Slf4j
public class HttpBinRestGateway extends RestGateway {

    private static final String GET_SLIDE_SHOW_DATA_KEY = "get-slide-show-data";

    public HttpBinRestGateway(final RestTemplate restTemplate, final ServiceEndpointProperties serviceEndpointProperties) {
        super(restTemplate, serviceEndpointProperties.getHttpBinService());
    }

    @Retry(name = "rest-default")
    public Optional<SlideShowData> getSlideShowData() {
        final URI uri = fromHttpUrl(getServiceEndpoint().getBaseUrl())
                .path(getServiceEndpoint().getPath(GET_SLIDE_SHOW_DATA_KEY))
                .build()
                .toUri();

        final Optional<SlideShowData> slideShowData = httpGet(uri, SlideShowData.class);
        return slideShowData;
    }
}
