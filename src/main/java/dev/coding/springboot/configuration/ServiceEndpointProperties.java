package dev.coding.springboot.configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties("service-endpoints")
public class ServiceEndpointProperties {

    @Getter
    @Setter
    private ServiceEndpoint github = new ServiceEndpoint();

}
