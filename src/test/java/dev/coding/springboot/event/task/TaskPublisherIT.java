package dev.coding.springboot.event.task;

import dev.coding.springboot.configuration.RabbitMQProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static dev.coding.springboot.TestConstants.ANY_EXCHANGE_NAME;
import static dev.coding.springboot.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;
import static dev.coding.springboot.TestConstants.ANY_TASK_NAME;
import static dev.coding.springboot.TestObjectFactory.anyTaskWithName;
import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.QueueBuilder.nonDurable;

@SpringBootTest
@DirtiesContext
public class TaskPublisherIT {

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
        rabbitMQProperties.getTaskReceived().setQueueName(ANY_QUEUE_NAME);
        rabbitMQProperties.getTaskReceived().setRoutingKey(ANY_ROUTING_KEY);

        taskPublisher = new TaskPublisher(rabbitTemplate, rabbitMQProperties);
    }

    @Test
    public void test() {
        final Task task = anyTaskWithName(ANY_TASK_NAME);
        rabbitTemplate.convertAndSend(ANY_EXCHANGE_NAME, ANY_ROUTING_KEY, task);
    }
}
