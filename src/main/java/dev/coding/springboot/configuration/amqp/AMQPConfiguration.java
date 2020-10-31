package dev.coding.springboot.configuration.amqp;

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
public class AMQPConfiguration {

    public static final String TASK_RECEIVED_QUEUE_NAME = "${rabbitmq.task-received.queue-name}";

    private final RabbitMQProperties rabbitMqProperties;
    private final MessageConfirmCallback messageConfirmCallback;

    public AMQPConfiguration(final RabbitMQProperties rabbitMqProperties, final MessageConfirmCallback messageConfirmCallback) {
        this.rabbitMqProperties = rabbitMqProperties;
        this.messageConfirmCallback = messageConfirmCallback;
    }

    @Bean
    public Exchange exchangeTasks() {
        return directExchange(rabbitMqProperties.getExchangeName()).build();
    }

    @Bean
    public Queue queueTaskReceived() {
        return durable(rabbitMqProperties.getTaskReceived().getQueueName())
                .deadLetterExchange(rabbitMqProperties.getExchangeName())
                .deadLetterRoutingKey(rabbitMqProperties.getTaskReceived().getRoutingKey())
                .build();
    }

    @Bean
    public Queue queueDeadLetter() {
        return durable(rabbitMqProperties.getDeadLetter().getQueueName()).build();
    }

    @Bean
    public Binding bindingTaskReceived() {
        return bind(queueTaskReceived())
                .to(exchangeTasks())
                .with(rabbitMqProperties.getTaskReceived().getRoutingKey())
                .noargs();
    }

    @Bean
    public Binding bindingDeadLetter() {
        return bind(queueDeadLetter())
                .to(exchangeTasks())
                .with(rabbitMqProperties.getDeadLetter().getRoutingKey())
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final CachingConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setConfirmCallback(messageConfirmCallback);
        return rabbitTemplate;
    }

}
