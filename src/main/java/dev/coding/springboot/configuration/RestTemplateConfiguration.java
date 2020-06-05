package dev.coding.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate getDefaultRestTemplate() {
        return RestTemplateFactory.getBasicRestTemplate();
    }
}
