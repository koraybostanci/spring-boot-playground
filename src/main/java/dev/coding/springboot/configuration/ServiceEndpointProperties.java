package dev.coding.springboot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
@Validated
@ConfigurationProperties("service-endpoints")
public class ServiceEndpointProperties {

    private ServiceEndpoint httpBin = new ServiceEndpoint();

    @Getter
    @Setter
    @Validated
    public static class ServiceEndpoint {
        @NotBlank
        private String baseUrl;
        private Map<String, String> paths;

        public String getPath(final String key) {
            return paths.get(key);
        }
    }
}
