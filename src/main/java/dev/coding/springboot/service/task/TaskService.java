package dev.coding.springboot.service.task;

import dev.coding.springboot.queue.inbox.Task;

public interface TaskService {

    void publish(final Task task);

}
