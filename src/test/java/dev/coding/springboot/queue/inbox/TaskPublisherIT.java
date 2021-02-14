package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.configuration.amqp.QueueProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static dev.coding.springboot.TestConstants.*;
import static dev.coding.springboot.TestObjectFactory.anyTaskWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.nonDurable;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskPublisherIT {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private QueueProperties queueProperties;
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

        taskPublisher = new TaskPublisher(rabbitTemplate, queueProperties);
    }

    @Test
    public void publish_successfullyPublishesTaskToExchange_whenGivenTaskIsValid() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        taskPublisher.publish(task);

        assertThat(getMessageCountInQueue(queueProperties.getInbox().getQueueName())).isOne();
    }

    @Test
    public void publish_doesNotPublishTaskToExchange_whenGivenTaskIsInvalid() {
        final Task task = anyTaskWithName(null);

        taskPublisher.publish(task);
        assertThat(getMessageCountInQueue(queueProperties.getInbox().getQueueName())).isZero();
    }

    private int getMessageCountInQueue(final String queueName) {
        return amqpAdmin.getQueueInfo(queueName).getMessageCount();
    }
}
