package dev.coding.springboot.event;

import dev.coding.springboot.configuration.MetricsCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static java.lang.System.currentTimeMillis;

@Slf4j
public abstract class EventPublisher<T> {

    private static final String PUBLISHING_EVENT= "Publishing event to rabbitmq [{}]";
    private static final String SUCCESSFULLY_PUBLISHED_EVENT = "Successfully published event to rabbitmq [{}]";

    private final Class<T> type;
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;
    private final MetricsCollector metricsCollector;

    public EventPublisher(final Class<T> type, final RabbitTemplate rabbitTemplate, final String exchangeName, final String routingKey, final MetricsCollector metricsCollector) {
        this.type = type;
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.metricsCollector = metricsCollector;
    }

    public abstract void publish(final T data);

    protected void doPublish(final T event) {
        final long startedAt = currentTimeMillis();
        log.debug(PUBLISHING_EVENT, event);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);

        log.debug(SUCCESSFULLY_PUBLISHED_EVENT, event);
        metricsCollector.trackEventPublishedDuration(currentTimeMillis() - startedAt, type.getSimpleName());
    }
}
