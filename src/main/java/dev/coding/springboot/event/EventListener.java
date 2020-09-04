package dev.coding.springboot.event;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

@FunctionalInterface
public interface EventListener {
    void onMessageReceived(final Message message, final @Header(DELIVERY_TAG) long deliveryTag, final Channel channel) throws IOException;
}
