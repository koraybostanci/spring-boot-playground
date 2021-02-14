package dev.coding.springboot.configuration.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Configuration
public class QueueConfiguration {

    public static final String INBOX_QUEUE_NAME = "${queues.inbox.queue-name}";

    private final QueueProperties queueProperties;

    public QueueConfiguration(final QueueProperties queueProperties) {
        this.queueProperties = queueProperties;
    }

    @Bean
    public Exchange exchange() {
        return directExchange(queueProperties.getExchangeName()).build();
    }

    @Bean
    public Queue queueInbox() {
        return durable(queueProperties.getInbox().getQueueName())
                .deadLetterExchange(queueProperties.getExchangeName())
                .deadLetterRoutingKey(queueProperties.getDeadLetter().getRoutingKey())
                .build();
    }

    @Bean
    public Queue queueDeadLetter() {
        return durable(queueProperties.getDeadLetter().getQueueName()).build();
    }

    @Bean
    public Binding bindingInbox() {
        return bind(queueInbox())
                .to(exchange())
                .with(queueProperties.getInbox().getRoutingKey())
                .noargs();
    }

    @Bean
    public Binding bindingDeadLetter() {
        return bind(queueDeadLetter())
                .to(exchange())
                .with(queueProperties.getDeadLetter().getRoutingKey())
                .noargs();
    }

}
