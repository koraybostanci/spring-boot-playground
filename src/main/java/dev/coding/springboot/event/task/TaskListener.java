package dev.coding.springboot.event.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TaskListener  {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskListener.class);
    private static final String TASKS_RECEIVED_QUEUE_NAME = "${rabbitmq.tasks-received.queue-name}";

    private final ObjectMapper objectMapper;

    public TaskListener(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = TASKS_RECEIVED_QUEUE_NAME)
    public void onMessage(final Message message) throws IOException {
        LOGGER.info("Message received [{}]", message);

        final Task task = objectMapper.readValue(message.getBody(), Task.class);
        LOGGER.info("Task [{}]", task);
    }
}
