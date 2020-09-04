package dev.coding.springboot.event.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.RabbitProperties;
import dev.coding.springboot.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher implements EventPublisher<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final ObjectMapper objectMapper;

    public MessagePublisher(final RabbitTemplate rabbitTemplate, final RabbitProperties rabbitProperties, final ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(final String data) {
        LOGGER.info("Publishing message to rabbitmq [{}]", data);

        try {
            rabbitTemplate.convertAndSend(rabbitProperties.getExchangeName(),
                    rabbitProperties.getMessageReceived().getRoutingKey(),
                    toMessage(data));
            LOGGER.info("Successfully published message to rabbitmq [{}]", data);
        } catch (JsonProcessingException | AmqpException ex) {
            LOGGER.error("Failed to publish message [{}] to rabbitmq due to [{}]", data, ex);
        }
    }

    private Message toMessage(final Object data) throws JsonProcessingException {
        return MessageBuilder
                .withBody(objectMapper.writeValueAsBytes(data))
                .build();
    }
}
