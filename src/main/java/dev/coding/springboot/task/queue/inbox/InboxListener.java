package dev.coding.springboot.task.queue.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.task.queue.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static dev.coding.springboot.common.configuration.rabbitmq.RabbitMQConfiguration.INBOX_QUEUE_NAME;

@Component
@Slf4j
public class InboxListener extends MessageListener<Task> {

    public InboxListener(final ObjectMapper objectMapper) {
        super(Task.class, objectMapper);
    }

    @Override
    @RabbitListener(queues = INBOX_QUEUE_NAME)
    public void onMessage(final Message message) throws IOException {
        log.debug("Message received [{}][{}]", message.hashCode(), message);
        final Task task = toObject(message);
        log.debug("Message converted to [{}][{}]", message.hashCode(), task);
    }

}
