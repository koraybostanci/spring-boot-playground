package dev.coding.springboot.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestObjectMapper {

    public static ObjectMapper getTestObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static <T> T toObject(final ObjectMapper objectMapper, final byte[] contentAsByteArray, final Class<T> type) throws IOException {
        return objectMapper.readValue(contentAsByteArray, type);
    }
}
