package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.MetricsCollector;
import dev.coding.springboot.configuration.RabbitMQProperties;
import dev.coding.springboot.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class TaskPublisher implements EventPublisher<Task> {

    private static final String PUBLISHING_TASK = "Publishing task to rabbitmq [{}]";
    private static final String SUCCESSFULLY_PUBLISHED_TASK = "Successfully published task to rabbitmq [{}]";
    private static final String FAILED_TO_PUBLISH_TASK = "Failed to publish task [%s] to rabbitmq due to [%s]";

    private final MetricsCollector metricsCollector;
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final RabbitMQProperties rabbitMqProperties, final MetricsCollector metricsCollector) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = rabbitMqProperties.getExchangeName();
        this.routingKey = rabbitMqProperties.getTaskReceived().getRoutingKey();
        this.metricsCollector = metricsCollector;
    }

    @Override
    public boolean publish(final Task task) {
        final long startedAt = currentTimeMillis();
        log.info(PUBLISHING_TASK, task);

        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, task);

            log.info(SUCCESSFULLY_PUBLISHED_TASK, task);
            metricsCollector.trackTaskPublished(currentTimeMillis() - startedAt);
            
            return true;
        } catch (final AmqpException ex) {
            log.error(format(FAILED_TO_PUBLISH_TASK, task, ex.getMessage()), ex);
        }

        return false;
    }
}
