package dev.coding.springboot.task.service;

import dev.coding.springboot.task.queue.inbox.Task;

public interface TaskService {

    void publish(final Task task);

}
