package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.RabbitMqProperties;
import dev.coding.springboot.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TaskPublisher implements EventPublisher<Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final RabbitMqProperties rabbitMqProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = rabbitMqProperties.getExchangeName();
        this.routingKey = rabbitMqProperties.getTasksReceived().getRoutingKey();
    }

    @Override
    public boolean publish(final Task task) {
        LOGGER.info("Publishing message to rabbitmq [{}]", task);

        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, task);
            LOGGER.info("Successfully published message to rabbitmq [{}]", task);
            return true;
        } catch (final AmqpException ex) {
            LOGGER.error("Failed to publish message [{}] to rabbitmq due to [{}]", task, ex);
        }
        return false;
    }
}
