package dev.coding.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static dev.coding.springboot.configuration.RestTemplateFactory.getBasicRestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate getDefaultRestTemplate() {
        return getBasicRestTemplate();
    }
}
