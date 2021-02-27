package dev.coding.springboot.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Slf4j
public abstract class MessageListener<T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public MessageListener(final Class<T> type, final ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Timed
    protected abstract void onMessage(final Message message) throws IOException;

    protected T toObject(final Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), type);
    }
}
