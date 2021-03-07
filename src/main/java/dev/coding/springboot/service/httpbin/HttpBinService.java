package dev.coding.springboot.service.httpbin;

import dev.coding.springboot.gateway.httpbin.data.SlideShowData;

import java.util.Optional;

public interface HttpBinService {

    Optional<SlideShowData> getSlideShowData();

}
