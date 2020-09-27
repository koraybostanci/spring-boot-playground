package dev.coding.springboot;

import dev.coding.springboot.configuration.RabbitMQProperties.Received;
import dev.coding.springboot.event.task.Task;

import static dev.coding.springboot.TestConstants.ANY_ROUTING_KEY;

public class TestObjectFactory {

    public static Received anyReceivedEntry() {
        final Received received = new Received();
        received.setRoutingKey(ANY_ROUTING_KEY);
        return received;
    }

    public static Task anyTaskWithName(final String name) {
        final Task task = new Task();
        task.setName(name);
        return task;
    }
}
