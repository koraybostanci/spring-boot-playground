package dev.coding.springboot.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class MetricsCollector {

    private static final String TASK_PUBLISHED_DURATION = "app.duration.taskPublished";
    private static final String TASK_CONSUMED_DURATION = "app.duration.taskConsumed";

    private final MeterRegistry meterRegistry;

    public MetricsCollector(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void trackTaskPublished(final long duration) {
        meterRegistry.timer(TASK_PUBLISHED_DURATION).record(duration, MILLISECONDS);
    }

    public void trackTaskConsumed(final long duration) {
        meterRegistry.timer(TASK_CONSUMED_DURATION).record(duration, MILLISECONDS);
    }
}
