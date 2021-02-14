package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.configuration.amqp.QueueProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.coding.springboot.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.TestObjectFactory.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Disabled
public class TaskPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private QueueProperties queueProperties;

    private TaskPublisher taskPublisher;

    @BeforeEach
    public void beforeEach() {
        when(queueProperties.getExchangeName()).thenReturn(ANY_EXCHANGE_NAME);
        when(queueProperties.getInbox()).thenReturn(anyInboxQueue());

        taskPublisher = new TaskPublisher(rabbitTemplate, queueProperties);
    }

    @Test
    public void publish_succeeds_whenNoAmqpException() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        taskPublisher.publish(task);

        verify(rabbitTemplate).convertAndSend(
                queueProperties.getExchangeName(),
                queueProperties.getInbox().getRoutingKey(),
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