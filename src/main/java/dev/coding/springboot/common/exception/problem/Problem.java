package dev.coding.springboot.common.exception.problem;

import lombok.Builder;
import lombok.Data;

import static java.lang.System.currentTimeMillis;

@Data
@Builder
public class Problem {
    private final long timestamp = currentTimeMillis();
    private String title;
    private int status;
    private String message;
}
