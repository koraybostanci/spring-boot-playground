package dev.coding.springboot;

import dev.coding.springboot.configuration.RabbitMQProperties.TasksReceived;
import dev.coding.springboot.event.task.Task;

import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static TasksReceived getTasksReceivedEntry() {
        final TasksReceived tasksReceived = new TasksReceived();
        tasksReceived.setRoutingKey(ANY_ROUTING_KEY);
        return tasksReceived;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
