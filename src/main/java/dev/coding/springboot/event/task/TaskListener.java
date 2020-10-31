package dev.coding.springboot.event.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coding.springboot.configuration.MetricsCollector;
import dev.coding.springboot.event.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static dev.coding.springboot.configuration.amqp.AMQPConfiguration.TASK_RECEIVED_QUEUE_NAME;
import static java.lang.System.currentTimeMillis;

@Component
@Slf4j
public class TaskListener extends EventListener<Task> {

    private final MetricsCollector metricsCollector;

    public TaskListener(final ObjectMapper objectMapper, final MetricsCollector metricsCollector) {
        super(Task.class, objectMapper);
        this.metricsCollector = metricsCollector;
    }

    @RabbitListener(queues = TASK_RECEIVED_QUEUE_NAME)
    public void onMessage(final Message message) throws IOException {
        final long startedAt = currentTimeMillis();
        log.debug("Message received [{}]", message);
        final Task task = fromMessage(message);
        metricsCollector.trackEventProcessedDuration(currentTimeMillis() - startedAt, Task.class.getSimpleName());
    }

}
