package dev.coding.springboot.service.httpbin;

import dev.coding.springboot.gateway.httpbin.HttpBinRestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.coding.springboot.configuration.CacheConfiguration.SLIDE_SHOW_DATA_CACHE_NAME;

@Service
public class HttpBinServiceImpl implements HttpBinService {

    private final HttpBinRestGateway httpBinRestGateway;

    public HttpBinServiceImpl(final HttpBinRestGateway httpBinRestGateway) {
        this.httpBinRestGateway = httpBinRestGateway;
    }

    @Cacheable(cacheNames = SLIDE_SHOW_DATA_CACHE_NAME, unless = "#result == null")
    public Optional<SlideShowData> getSlideShowData() {
        return httpBinRestGateway.getSlideShowData();
    }

}
