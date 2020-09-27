package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.RabbitMQProperties;
import dev.coding.springboot.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class TaskPublisher implements EventPublisher<Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPublisher.class);

    private static final String PUBLISHING_TASK = "Publishing task to rabbitmq [{}]";
    private static final String SUCCESSFULLY_PUBLISHED_TASK = "Successfully published task to rabbitmq [{}]";
    private static final String FAILED_TO_PUBLISH_TASK = "Failed to publish task [%s] to rabbitmq due to [%s]";

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final RabbitMQProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = rabbitMqProperties.getExchangeName();
        this.routingKey = rabbitMqProperties.getReceived().getRoutingKey();
    }

    @Override
    public boolean publish(final Task task) {
        LOGGER.info(PUBLISHING_TASK, task);

        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, task);
            LOGGER.info(SUCCESSFULLY_PUBLISHED_TASK, task);
            return true;
        } catch (final AmqpException ex) {
            LOGGER.error(format(FAILED_TO_PUBLISH_TASK, task, ex.getMessage()), ex);
        }
        return false;
    }
}
