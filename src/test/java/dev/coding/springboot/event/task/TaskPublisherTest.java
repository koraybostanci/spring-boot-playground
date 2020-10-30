package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.MetricsCollector;
import dev.coding.springboot.configuration.amqp.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static dev.coding.springboot.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.TestObjectFactory.anyTaskWithName;
import static dev.coding.springboot.TestObjectFactory.anyTaskReceived;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
public class TaskPublisherTest {
    @Mock
    private MetricsCollector metricsCollector;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private RabbitMQProperties rabbitMqProperties;

    private TaskPublisher taskPublisher;

    @BeforeEach
    public void beforeEach() {
        when(rabbitMqProperties.getExchangeName()).thenReturn(ANY_EXCHANGE_NAME);
        when(rabbitMqProperties.getTaskReceived()).thenReturn(anyTaskReceived());

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMqProperties, metricsCollector);
    }

    @Test
    public void publish_succeeds_whenNoAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        taskPublisher.publish(task);

        verify(rabbitTemplate).convertAndSend(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getTaskReceived().getRoutingKey(),
                task);
        verify(metricsCollector).trackEventPublishedDuration(anyLong(), anyString());
    }

    @Test
    public  void publish_fails_whenAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        doThrow(AmqpException.class)
                .when(rabbitTemplate)
                .convertAndSend(anyString(), anyString(), any(Task.class));

        taskPublisher.publish(task);

        verifyNoInteractions(metricsCollector);
    }
}