package dev.coding.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("service-endpoints")
public class ServiceEndpointProperties {

    private ServiceEndpoint github = new ServiceEndpoint();

    public ServiceEndpoint getGithub() {
        return github;
    }
}
