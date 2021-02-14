package dev.coding.springboot.service;

import dev.coding.springboot.queue.inbox.Task;
import dev.coding.springboot.queue.inbox.TaskPublisher;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskPublisher taskPublisher;

    public TaskService(final TaskPublisher taskPublisher) {
        this.taskPublisher = taskPublisher;
    }

    public void publish(final Task task) {
        taskPublisher.publish(task);
    }

}
