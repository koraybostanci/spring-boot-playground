package dev.coding.springboot.queue.inbox;

import dev.coding.springboot.configuration.amqp.QueueProperties;
import dev.coding.springboot.queue.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskPublisher extends MessagePublisher<Task> {

    public TaskPublisher(final RabbitTemplate rabbitTemplate, final QueueProperties queueProperties) {
        super(rabbitTemplate, queueProperties.getExchangeName(), queueProperties.getInbox().getRoutingKey());
    }

    @Override
    public void publish(final Task task) {
        final boolean success = doPublish(task);
        log.info("Published successfully [%s]", task);
    }
}
