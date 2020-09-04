package dev.coding.springboot.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;
import static org.springframework.amqp.core.BindingBuilder.bind;

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
        return bind(queueMessageReceived())
                .to(exchangeMessages())
                .with(rabbitProperties.getMessages().getRoutingKey())
                .noargs();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory createMessagesRabbitListenerContainerFactory() {
        return createSimpleRabbitListenerContainerFactory(
                rabbitProperties.getMessages().getConcurrentConsumerCount());
    }

    public SimpleRabbitListenerContainerFactory createSimpleRabbitListenerContainerFactory(final int concurrentConsumerCount) {
        final SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConcurrentConsumers(concurrentConsumerCount);
        containerFactory.setPrefetchCount(1);
        containerFactory.setAcknowledgeMode(MANUAL);
        containerFactory.setChannelTransacted(true);
        return containerFactory;
    }

}
