package dev.coding.springboot.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ ServiceEndpointProperties.class })
public class SpringConfigurationProperties {

}
