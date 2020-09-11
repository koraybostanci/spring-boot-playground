package dev.coding.springboot;

import dev.coding.springboot.configuration.RabbitMqProperties;
import dev.coding.springboot.event.task.Task;

import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static RabbitMqProperties.TasksReceived getTasksReceivedEntry() {
        final RabbitMqProperties.TasksReceived tasksReceived = new RabbitMqProperties.TasksReceived();
        tasksReceived.setRoutingKey(ANY_ROUTING_KEY);
        return tasksReceived;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
