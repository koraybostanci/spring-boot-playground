package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.MetricsCollector;
import dev.coding.springboot.configuration.amqp.RabbitMQProperties;
import dev.coding.springboot.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class TaskPublisher extends EventPublisher<Task> {

    private static final String FAILED_TO_PUBLISH_TASK = "Failed to publish task [%s] to rabbitmq due to [%s]";

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final RabbitMQProperties rabbitMqProperties, final MetricsCollector metricsCollector) {
        super(Task.class, rabbitTemplate, rabbitMqProperties.getExchangeName(), rabbitMqProperties.getTaskReceived().getRoutingKey(), metricsCollector);
    }

    @Override
    public void publish(final Task data) {
        try {
            doPublish(data);
        } catch (final AmqpException ex) {
            log.error(format(FAILED_TO_PUBLISH_TASK, data, ex.getMessage()), ex);
        }
    }
}
