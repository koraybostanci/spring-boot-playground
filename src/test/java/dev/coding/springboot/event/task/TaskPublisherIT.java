package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.MetricsCollector;
import dev.coding.springboot.configuration.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.coding.springboot.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;
import static dev.coding.springboot.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.TestObjectFactory.anyTaskReceived;
import static dev.coding.springboot.TestObjectFactory.anyTaskWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.nonDurable;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@Disabled
public class TaskPublisherIT {
    @Mock
    private MetricsCollector metricsCollector;
    @Mock
    private RabbitMQProperties rabbitMQProperties;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;
    private TaskPublisher taskPublisher;

    @BeforeEach
    public void beforeEach() {
        final Exchange exchange = directExchange(ANY_EXCHANGE_NAME).build();
        final Queue queue = nonDurable(ANY_QUEUE_NAME).build();
        final Binding binding = bind(queue).to(exchange).with(ANY_ROUTING_KEY).noargs();

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        amqpAdmin.purgeQueue(ANY_QUEUE_NAME, false);

        rabbitMQProperties.setExchangeName(ANY_EXCHANGE_NAME);
        rabbitMQProperties.setTaskReceived(anyTaskReceived());

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMQProperties, metricsCollector);
    }

    @Test
    public void publish_successfullyPublishesTaskToExchange_whenGivenTaskIsValid() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        final boolean result = taskPublisher.publish(task);

        assertThat(result).isTrue();
    }

    @Test
    public void publish_doesNotPublishTaskToExchange_whenGivenTaskIsInvalid() {
        final Task task = anyTaskWithName(null);

        final boolean result = taskPublisher.publish(task);
        final int messageCount = getMessageCountInQueue();

        assertThat(result).isFalse();
        assertThat(messageCount).isZero();
    }

    private int getMessageCountInQueue() {
        return amqpAdmin.getQueueInfo(rabbitMQProperties.getTaskReceived().getQueueName())
                .getMessageCount();
    }
}
