package dev.coding.springboot.event.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageReceivedListener implements ChannelAwareMessageListener {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageReceivedListener.class);
    private final ObjectMapper objectMapper;

    public MessageReceivedListener(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.message-received.queue-name}", containerFactory = "messagesRabbitListenerContainerFactory")
    @Override
    public void onMessage(final Message message, final Channel channel) throws IOException {
        LOGGER.debug("Message received [{}]", message);

        final String data = objectMapper.readValue(message.getBody(), String.class);
        LOGGER.info("Message body [{}]", data);

        acknowledge(channel, message.getMessageProperties().getDeliveryTag(), data);
    }

    private void acknowledge(final Channel channel, final long deliveryTag, final String data) throws IOException {
        if (data.contains("n")) {
            channel.basicNack(deliveryTag, false, false);
            LOGGER.debug("NACK [{}]", data);
        } else {
            channel.basicAck(deliveryTag, false);
            LOGGER.debug("ACK [{}]", data);
        }
    }

}
