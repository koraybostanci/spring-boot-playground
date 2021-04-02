package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.base.AbstractIntegrationTest;
import dev.coding.springboot.configuration.rabbitmq.RabbitMQProperties;
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

import static dev.coding.springboot.common.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.common.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.common.TestConstants.ANY_ROUTING_KEY;
import static dev.coding.springboot.common.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.common.TestConstants.PROFILE_INTEGRATION_TEST;
import static dev.coding.springboot.common.TestObjectFactory.anyTaskWithName;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.nonDurable;

@ActiveProfiles(PROFILE_INTEGRATION_TEST)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskPublisherIT extends AbstractIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQProperties rabbitMQProperties;
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
        rabbitMQProperties.getInbox().setQueueName(ANY_QUEUE_NAME);
        rabbitMQProperties.getInbox().setRoutingKey(ANY_ROUTING_KEY);

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMQProperties);
    }

    @Test
    public void publish_successfullyPublishesTask_whenGivenTaskIsValid() throws InterruptedException {
        final Task task = anyTaskWithName(ANY_TASK_NAME);

        taskPublisher.publish(task);

        sleep(1000);

        assertThat(getMessageCountInQueue(ANY_QUEUE_NAME)).isOne();
    }

    private int getMessageCountInQueue(final String queueName) {
        return amqpAdmin.getQueueInfo(queueName).getMessageCount();
    }
}
