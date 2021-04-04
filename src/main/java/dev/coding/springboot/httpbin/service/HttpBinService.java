package dev.coding.springboot.httpbin.service;

import dev.coding.springboot.httpbin.gateway.data.SlideShowData;

import java.util.Optional;

public interface HttpBinService {

    Optional<SlideShowData> getSlideShowData();

}
