package dev.coding.springboot.configuration;

import org.springframework.validation.annotation.Validated;
import java.util.Map;

@Validated
public class ServiceEndpoint {

    private String baseUrl;
    private Map<String, String> paths;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, String> paths) {
        this.paths = paths;
    }
}
