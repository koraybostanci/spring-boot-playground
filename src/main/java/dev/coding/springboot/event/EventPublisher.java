package dev.coding.springboot.event;

@FunctionalInterface
public interface EventPublisher<T> {
    boolean publish(final T data);
}
