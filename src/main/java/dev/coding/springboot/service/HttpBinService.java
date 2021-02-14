package dev.coding.springboot.service;

import dev.coding.springboot.gateway.httpbin.HttpBinRestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.springframework.stereotype.Service;

@Service
public class HttpBinService {

    private final HttpBinRestGateway httpBinRestGateway;

    public HttpBinService(final HttpBinRestGateway httpBinRestGateway) {
        this.httpBinRestGateway = httpBinRestGateway;
    }

    public SlideShowData getSlideShowData() {
        return httpBinRestGateway.getSlideShowData().get();
    }
}
