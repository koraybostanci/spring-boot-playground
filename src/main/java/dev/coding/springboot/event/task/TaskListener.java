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
    private static final String TASK_RECEIVED_QUEUE_NAME = "${rabbitmq.task-received.queue-name}";

    private final ObjectMapper objectMapper;

    public TaskListener(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = TASK_RECEIVED_QUEUE_NAME)
    public void onMessage(final Message message) throws IOException {
        LOGGER.debug("Message received [{}]", message);
        final Task task = toTask(message);
        LOGGER.info("Message converted to task [{}]", task);
    }

    private Task toTask(final Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), Task.class);
    }

}
