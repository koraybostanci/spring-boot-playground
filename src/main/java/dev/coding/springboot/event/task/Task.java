package dev.coding.springboot.event.task;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@JsonIdentityInfo(
        generator = UUIDGenerator.class,
        property = "id",
        scope = Task.class)
@ToString
@EqualsAndHashCode
public class Task {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
