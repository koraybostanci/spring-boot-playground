package dev.coding.springboot;

import dev.coding.springboot.configuration.amqp.QueueProperties.Inbox;
import dev.coding.springboot.queue.inbox.Task;

import static dev.coding.springboot.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static Inbox anyInboxQueue() {
        final Inbox inbox = new Inbox();
        inbox.setQueueName(ANY_QUEUE_NAME);
        inbox.setRoutingKey(ANY_ROUTING_KEY);
        return inbox;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
