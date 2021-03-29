package dev.coding.springboot.rest.controller;

import dev.coding.springboot.gateway.httpbin.data.SlideShowData;
import dev.coding.springboot.service.httpbin.HttpBinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class HttpBinController {

    private final HttpBinService httpBinService;

    public HttpBinController(final HttpBinService httpBinService) {
        this.httpBinService = httpBinService;
    }

    @GetMapping(value = "/slides", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SlideShowData> getSlideShowData() {
        final Optional<SlideShowData> data = httpBinService.getSlideShowData();
        return data.isPresent() ? ok(data.get()) : notFound().build();
    }
}
