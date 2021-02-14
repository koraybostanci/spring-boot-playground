package dev.coding.springboot.configuration;

import dev.coding.springboot.configuration.amqp.QueueProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ServiceEndpointProperties.class,
        QueueProperties.class
})
public class SpringConfigurationProperties {
}
