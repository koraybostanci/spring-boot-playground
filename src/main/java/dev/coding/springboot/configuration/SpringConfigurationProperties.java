package dev.coding.springboot.configuration;

import dev.coding.springboot.configuration.rabbitmq.RabbitMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ServiceEndpointProperties.class,
        RabbitMQProperties.class
})
public class SpringConfigurationProperties {
}
