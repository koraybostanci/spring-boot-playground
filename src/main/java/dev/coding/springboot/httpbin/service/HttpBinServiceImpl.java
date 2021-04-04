package dev.coding.springboot.httpbin.service;

import dev.coding.springboot.httpbin.gateway.HttpBinRestGateway;
import dev.coding.springboot.httpbin.gateway.data.SlideShowData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.coding.springboot.common.configuration.CacheConfiguration.SLIDE_SHOW_DATA_CACHE_NAME;

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
