package dev.coding.springboot.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static java.lang.String.format;

@Slf4j
public abstract class MessagePublisher<T> {

    private static final String PUBLISHING_MESSAGE = "Publishing message [{}] to [{}]";
    private static final String SUCCESSFULLY_PUBLISHED_MESSAGE = "Successfully published message [{}] to [{}]";
    private static final String FAILED_WHILE_PUBLISHING_MESSAGE = "Failed while publishing message [%s] to [%s] due to [%s]";

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public MessagePublisher(final RabbitTemplate rabbitTemplate, final String exchangeName, final String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public abstract void publish(final T data);

    protected boolean doPublish(final T message) {
        try {
            log.debug(PUBLISHING_MESSAGE, message, exchangeName);
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            log.info(SUCCESSFULLY_PUBLISHED_MESSAGE, message, exchangeName);
        } catch (final AmqpException ex) {
            log.error(format(FAILED_WHILE_PUBLISHING_MESSAGE, message, exchangeName, ex));
            return false;
        }
        return true;
    }
}
