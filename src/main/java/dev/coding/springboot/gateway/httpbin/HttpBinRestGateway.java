package dev.coding.springboot.gateway.httpbin;

import dev.coding.springboot.configuration.ServiceEndpointProperties;
import dev.coding.springboot.gateway.RestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
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
        super(restTemplate, serviceEndpointProperties.getHttpBin());
    }

    public Optional<SlideShowData> getSlideShowData() {
        final URI uri = fromHttpUrl(getServiceEndpoint().getBaseUrl())
                .path(getServiceEndpoint().getPath(GET_SLIDE_SHOW_DATA_KEY))
                .build()
                .toUri();

        final Optional<SlideShowData> slideShowData = httpGet(uri, SlideShowData.class);
        return slideShowData;
    }
}
