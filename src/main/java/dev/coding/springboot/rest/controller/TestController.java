package dev.coding.springboot.rest.controller;

import dev.coding.springboot.event.EventPublisher;
import dev.coding.springboot.event.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final EventPublisher<Task> taskPublisher;

    public TestController(EventPublisher<Task> taskPublisher) {
        this.taskPublisher = taskPublisher;
    }

    @GetMapping("/test")
    public String test() {
        return "Ok";
    }

    @GetMapping("/send/{name}")
    public void sendTask(@PathVariable("name") String name) {
        final Task task = new Task();
        task.setName(name);
        taskPublisher.publish(task);
    }

}
