package dev.coding.springboot.event;

@FunctionalInterface
public interface EventPublisher<T> {
    void publish(final T data);
}
