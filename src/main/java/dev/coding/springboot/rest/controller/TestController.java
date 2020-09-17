package dev.coding.springboot.rest.controller;

import dev.coding.springboot.event.EventPublisher;
import dev.coding.springboot.event.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @GetMapping("/task/{name}")
    public void sendTask(@Valid @PathVariable("name") final String name,
                         @Valid @RequestParam(value = "count", defaultValue = "1") final int count) {
        for (int i = 0; i < count; ++i) {
            final Task task = new Task();
            task.setName(name);
            taskPublisher.publish(task);
        }
    }

}
