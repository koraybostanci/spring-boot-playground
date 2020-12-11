package dev.coding.springboot.configuration.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    public static final String REDIS_TEMPLATE_AS_OBJECT_VALUE = "redisTemplateAsObjectValue";

    @Bean
    @Qualifier(REDIS_TEMPLATE_AS_OBJECT_VALUE)
    public RedisTemplate<String, Object> redisTemplateAsString(final ObjectMapper objectMapper, final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JsonRedisSerializer(objectMapper, Object.class));
        return redisTemplate;
    }

}
