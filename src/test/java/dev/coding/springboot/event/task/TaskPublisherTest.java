package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static dev.coding.springboot.TestConstants.*;
import static dev.coding.springboot.TestObjectFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMQProperties rabbitMqProperties;

    private TaskPublisher taskPublisher;

    @BeforeEach
    public void beforeEach() {
        when(rabbitMqProperties.getExchangeName()).thenReturn(ANY_EXCHANGE_NAME);
        when(rabbitMqProperties.getTasksReceived()).thenReturn(anyTasksReceivedEntry());

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMqProperties);
    }

    @Test
    void publish_succeeds_whenNoAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        final boolean result = taskPublisher.publish(task);

        assertThat(result).isTrue();
        verify(rabbitTemplate).convertAndSend(rabbitMqProperties.getExchangeName(), rabbitMqProperties.getTasksReceived().getRoutingKey(), task);
    }

    @Test
    void publish_fails_whenAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        doThrow(AmqpException.class).when(rabbitTemplate).convertAndSend(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getTasksReceived().getRoutingKey(),
                task);

        final boolean result = taskPublisher.publish(task);

        assertThat(result).isFalse();
    }
}