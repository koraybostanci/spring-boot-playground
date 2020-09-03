package dev.coding.springboot.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

@Component
public class EventListener {

    private static Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    private final ObjectMapper objectMapper;

    public EventListener(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.messages.queue-name}")
    public void onMessageReceived(final Message message, final @Header(DELIVERY_TAG) long deliveryTag, final Channel channel) throws IOException {
        LOGGER.info("Channel [{}]", channel);
        LOGGER.info("DeliveryTag [{}]", deliveryTag);
        LOGGER.info("Message received [{}]", message);
        LOGGER.info("Message body [{}]", objectMapper.readValue(message.getBody(), String.class));
    }

}
