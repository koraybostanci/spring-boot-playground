package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.configuration.rabbitmq.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.coding.springboot.common.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.common.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.common.TestObjectFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Disabled
public class TaskPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private RabbitMQProperties rabbitMQProperties;

    private TaskPublisher taskPublisher;

    @BeforeEach
    public void beforeEach() {
        when(rabbitMQProperties.getExchangeName()).thenReturn(ANY_EXCHANGE_NAME);
        when(rabbitMQProperties.getInbox()).thenReturn(anyInboxQueue());

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMQProperties);
    }

    @Test
    public void publish_succeeds_whenNoAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        taskPublisher.publish(task);

        verify(rabbitTemplate).convertAndSend(
                rabbitMQProperties.getExchangeName(),
                rabbitMQProperties.getInbox().getRoutingKey(),
                task);
    }

    @Test
    public void publish_fails_whenAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        doThrow(AmqpException.class)
                .when(rabbitTemplate)
                .convertAndSend(anyString(), anyString(), any(Task.class));

        taskPublisher.publish(task);
    }
}