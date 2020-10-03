package dev.coding.springboot;

import dev.coding.springboot.configuration.RabbitMQProperties.TaskReceived;
import dev.coding.springboot.event.task.Task;

import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static TaskReceived anyTaskReceivedEntry() {
        final TaskReceived taskReceived = new TaskReceived();
        taskReceived.setRoutingKey(ANY_ROUTING_KEY);
        return taskReceived;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
