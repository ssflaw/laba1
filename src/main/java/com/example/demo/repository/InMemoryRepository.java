package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryRepository<T> {

    private final Map<Long, T> storage = new ConcurrentSkipListMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    /** Возвращает копию сущности с указанным id. */
    protected abstract T withId(T entity, Long id);

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public T create(T entity) {
        Long id = sequence.incrementAndGet();
        T saved = withId(entity, id);
        storage.put(id, saved);
        return saved;
    }

    public T update(Long id, T entity) {
        T saved = withId(entity, id);
        storage.put(id, saved);
        return saved;
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}
