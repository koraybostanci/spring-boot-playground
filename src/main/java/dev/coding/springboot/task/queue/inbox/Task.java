package dev.coding.springboot.task.queue.inbox;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class Task {
    @NotNull
    private String name;
}

