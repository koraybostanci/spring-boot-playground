package dev.coding.springboot.service.httpbin;

import dev.coding.springboot.gateway.httpbin.HttpBinRestGateway;
import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HttpBinServiceImpl implements HttpBinService {

    private final HttpBinRestGateway httpBinRestGateway;

    public HttpBinServiceImpl(final HttpBinRestGateway httpBinRestGateway) {
        this.httpBinRestGateway = httpBinRestGateway;
    }

    public Optional<SlideShowData> getSlideShowData() {
        return httpBinRestGateway.getSlideShowData();
    }

}
