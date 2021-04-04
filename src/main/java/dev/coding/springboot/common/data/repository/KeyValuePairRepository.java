package dev.coding.springboot.common.data.repository;

import java.util.Optional;

public interface KeyValuePairRepository<K,V> {
    void save(K key, V value);

    Optional<V> get(K key);

    void delete(K key);
}
