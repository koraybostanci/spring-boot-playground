package dev.coding.springboot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
public class AmqpConfiguration {

    private final RabbitProperties rabbitProperties;

    public AmqpConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Exchange exchangeMessages() {
        return new DirectExchange(rabbitProperties.getExchangeName(), true, false);
    }

    @Bean
    public Queue queueMessageReceived() {
        return new Queue(rabbitProperties.getMessages().getQueueName(), true);
    }

    @Bean
    public Binding bindingMessageReceived() {
        return new Binding(rabbitProperties.getMessages().getQueueName(),
                QUEUE,
                rabbitProperties.getExchangeName(),
                rabbitProperties.getMessages().getRoutingKey(),null);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory createSimpleRabbitListenerContainerFactory() {
        final SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConcurrentConsumers(10);
        containerFactory.setPrefetchCount(1);
        containerFactory.setAcknowledgeMode(MANUAL);
        return containerFactory;
    }

}
