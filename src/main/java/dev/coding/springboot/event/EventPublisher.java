package dev.coding.springboot.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class EventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final ObjectMapper objectMapper;

    public EventPublisher(final RabbitTemplate rabbitTemplate, final RabbitProperties rabbitProperties, final ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
        this.objectMapper = objectMapper;
    }

    public void publish(final Object data) {
        LOGGER.info("Publishing message to rabbitmq [{}]", data);

        final Optional<Message> optionalMessage = toMessage(data);
        if (optionalMessage.isEmpty()) {
            return;
        }

        try {
            rabbitTemplate.convertAndSend(rabbitProperties.getExchangeName(),
                    rabbitProperties.getMessages().getRoutingKey(),
                    optionalMessage.get());
            LOGGER.info("Successfully published message to rabbitmq [{}]", data);
        } catch (AmqpException ex) {
            LOGGER.error("Failed to publish message [{}] to rabbitmq due to [{}]", data, ex);
        }
    }

    private Optional<Message> toMessage(final Object data) {
        try {
            final Message message = MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(data))
                    .build();
            return of(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to convert message [{}] to Message", data);
        }
        return empty();
    }
}
