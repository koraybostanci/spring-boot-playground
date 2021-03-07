package dev.coding.springboot.rest.controller;

import dev.coding.springboot.queue.inbox.Task;
import dev.coding.springboot.service.task.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/health", produces = TEXT_PLAIN_VALUE)
    public String health() {
        return "Ok";
    }

    @PostMapping("/task/{name}")
    public void sendTask(@Valid @PathVariable("name") final String name) {
        final Task task = new Task();
        task.setName(name);
        taskService.publish(task);
    }
}
