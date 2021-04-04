package dev.coding.springboot.common.configuration.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public JsonRedisSerializer(final ObjectMapper objectMapper, final Class<T> type) {
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public byte[] serialize(final T object) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (final JsonProcessingException ex) {
            throw new SerializationException(ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(final byte[] bytes) throws SerializationException {
        try {
            return objectMapper.readValue(bytes, type);
        } catch (final IOException ex) {
            throw new SerializationException(ex.getMessage(), ex);
        }
    }
}
