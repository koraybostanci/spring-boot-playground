package dev.coding.springboot.configuration;

import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@ConfigurationProperties("service-endpoints")
@NoArgsConstructor
public class ServiceEndpointProperties {

    @Valid
    private ServiceEndpoint quote = new ServiceEndpoint();

    public ServiceEndpoint getQuote() {
        return quote;
    }

}
