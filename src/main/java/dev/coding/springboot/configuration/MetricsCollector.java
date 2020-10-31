package dev.coding.springboot.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class MetricsCollector {

    public static final String EVENTS_PUBLISHED_DURATION = "app.events.published.duration";
    public static final String EVENTS_PROCESSED_DURATION = "app.events.processed.duration";

    private final MeterRegistry meterRegistry;

    public MetricsCollector(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void trackEventPublishedDuration(final long duration, final String eventType) {
        meterRegistry.timer(EVENTS_PUBLISHED_DURATION, "eventType", eventType).record(duration, MILLISECONDS);
    }

    public void trackEventProcessedDuration(final long duration, final String eventType) {
        meterRegistry.timer(EVENTS_PROCESSED_DURATION,"eventType", eventType).record(duration, MILLISECONDS);
    }
}
