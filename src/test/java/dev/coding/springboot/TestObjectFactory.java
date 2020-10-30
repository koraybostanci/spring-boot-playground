package dev.coding.springboot;

import dev.coding.springboot.configuration.amqp.RabbitMQProperties.TaskReceived;
import dev.coding.springboot.event.task.Task;

import static dev.coding.springboot.TestConstants.ANY_QUEUE_NAME;
import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static TaskReceived anyTaskReceived() {
        final TaskReceived taskReceived = new TaskReceived();
        taskReceived.setQueueName(ANY_QUEUE_NAME);
        taskReceived.setRoutingKey(ANY_ROUTING_KEY);
        return taskReceived;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
