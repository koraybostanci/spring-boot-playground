package dev.coding.springboot.common.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Configuration
public class RabbitMQConfiguration {

    public static final String INBOX_QUEUE_NAME = "${rabbitmq.inbox.queue-name}";

    private final RabbitMQProperties rabbitMQProperties;

    public RabbitMQConfiguration(final RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public Exchange exchange() {
        return directExchange(rabbitMQProperties.getExchangeName()).build();
    }

    @Bean
    public Queue queueInbox() {
        return durable(rabbitMQProperties.getInbox().getQueueName())
                .deadLetterExchange(rabbitMQProperties.getExchangeName())
                .deadLetterRoutingKey(rabbitMQProperties.getDeadLetter().getRoutingKey())
                .build();
    }

    @Bean
    public Queue queueDeadLetter() {
        return durable(rabbitMQProperties.getDeadLetter().getQueueName()).build();
    }

    @Bean
    public Binding bindingInbox() {
        return bind(queueInbox())
                .to(exchange())
                .with(rabbitMQProperties.getInbox().getRoutingKey())
                .noargs();
    }

    @Bean
    public Binding bindingDeadLetter() {
        return bind(queueDeadLetter())
                .to(exchange())
                .with(rabbitMQProperties.getDeadLetter().getRoutingKey())
                .noargs();
    }

    @Bean
    public RabbitTemplate defaultRabbitTemplate(final CachingConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
