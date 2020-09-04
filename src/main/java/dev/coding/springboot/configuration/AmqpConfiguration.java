package dev.coding.springboot.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

@Configuration
public class AmqpConfiguration {

    private final RabbitProperties rabbitProperties;

    public AmqpConfiguration(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Bean
    public Exchange exchangeMessages() {
        return directExchange(rabbitProperties.getExchangeName()).build();
    }

    @Bean
    public Queue queueMessageReceived() {
        return durable(rabbitProperties.getMessageReceived().getQueueName())
                .deadLetterExchange(rabbitProperties.getExchangeName())
                .deadLetterRoutingKey(rabbitProperties.getMessageDeadLetter().getRoutingKey())
                .build();
    }

    @Bean
    public Queue queueMessageDeadLetter() {
        return durable(rabbitProperties.getMessageDeadLetter().getQueueName()).build();
    }

    @Bean
    public Binding bindingMessageReceived() {
        return bind(queueMessageReceived())
                .to(exchangeMessages())
                .with(rabbitProperties.getMessageReceived().getRoutingKey())
                .noargs();
    }

    @Bean
    public Binding bindingDeadLetter() {
        return bind(queueMessageDeadLetter())
                .to(exchangeMessages())
                .with(rabbitProperties.getMessageDeadLetter().getRoutingKey())
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    @Bean(name = "messagesRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory createMessagesRabbitListenerContainerFactory() {
        return createSimpleRabbitListenerContainerFactory(
                rabbitProperties.getMessageReceived().getConcurrentConsumerCount());
    }

    public SimpleRabbitListenerContainerFactory createSimpleRabbitListenerContainerFactory(final int concurrentConsumerCount) {
        final SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory());
        containerFactory.setConcurrentConsumers(concurrentConsumerCount);
        containerFactory.setPrefetchCount(1);
        containerFactory.setAcknowledgeMode(MANUAL);
        containerFactory.setChannelTransacted(true);
        return containerFactory;
    }

}
