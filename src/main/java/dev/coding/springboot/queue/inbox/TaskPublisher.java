package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.configuration.rabbitmq.RabbitMQProperties;
import dev.coding.springboot.queue.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskPublisher extends MessagePublisher<Task> {

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final RabbitMQProperties rabbitMQProperties) {
        super(rabbitTemplate, rabbitMQProperties.getExchangeName(), rabbitMQProperties.getInbox().getRoutingKey());
    }

    @Override
    public void publish(final Task task) {
        doPublish(task);
    }
}
