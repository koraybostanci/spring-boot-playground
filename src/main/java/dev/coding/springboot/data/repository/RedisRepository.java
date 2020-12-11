package dev.coding.springboot.data.repository;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public abstract class RedisRepository<K,V> implements KeyValuePairRepository<K,V> {

    private final RedisTemplate<K,V> redisTemplate;

    public RedisRepository(final RedisTemplate<K,V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(final K key, final V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Optional<V> get(final K key) {
        return ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(final K key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }
}
