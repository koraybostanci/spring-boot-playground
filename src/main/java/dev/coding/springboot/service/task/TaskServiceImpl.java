package dev.coding.springboot.service.task;

import dev.coding.springboot.queue.inbox.Task;
import dev.coding.springboot.queue.inbox.TaskPublisher;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskPublisher taskPublisher;

    public TaskServiceImpl(final TaskPublisher taskPublisher) {
        this.taskPublisher = taskPublisher;
    }

    public void publish(final Task task) {
        taskPublisher.publish(task);
    }

}
