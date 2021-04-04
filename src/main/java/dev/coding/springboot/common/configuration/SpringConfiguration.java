package dev.coding.springboot.common.configuration;

import dev.coding.springboot.common.configuration.rabbitmq.RabbitMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        ServiceEndpointProperties.class,
        RabbitMQProperties.class
})
@Configuration
public class SpringConfiguration {
}
