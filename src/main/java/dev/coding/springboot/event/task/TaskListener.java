package dev.coding.springboot.event.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.MetricsCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.lang.System.currentTimeMillis;

@Component
@Slf4j
public class TaskListener  {

    private static final String TASK_RECEIVED_QUEUE_NAME = "${rabbitmq.task-received.queue-name}";

    private final MetricsCollector metricsCollector;
    private final ObjectMapper objectMapper;

    public TaskListener(final ObjectMapper objectMapper, final MetricsCollector metricsCollector) {
        this.objectMapper = objectMapper;
        this.metricsCollector = metricsCollector;
    }

    @RabbitListener(queues = TASK_RECEIVED_QUEUE_NAME)
    public void onMessage(final Message message) throws IOException {
        final long startedAt = currentTimeMillis();
        log.debug("Message received [{}]", message);
        final Task task = toTask(message);
        log.info("Message converted to task [{}]", task);
        metricsCollector.trackTaskConsumed(currentTimeMillis() - startedAt);

    }

    private Task toTask(final Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), Task.class);
    }

}
