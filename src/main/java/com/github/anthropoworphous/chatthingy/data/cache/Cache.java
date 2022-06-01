package com.github.anthropoworphous.chatthingy.data.cache;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface Cache<K, V> extends Map<K, V> {
    // TODO cache factory

    default void regulateSize() {
        if (limit() == -1) { return; }
        for (int i = limit() - cache().size(); i < 0; i++) {
            cache().remove(removedTarget());
        }
    }

    Map<K, V> cache();
    int limit();
    void limit(int limit);

    K removedTarget();
    default Optional<V> opGet(K key) { return Optional.ofNullable(cache().get(key)); }
    default V cache(K key, Function<K, V> getter) {
        return Optional.ofNullable(cache().get(key)).orElseGet(() -> {
            V v = getter.apply(key);
            put(key, v);
            return v;
        });
    }

    @Override
    default V put(K key, @Nullable V value) {
        if (value == null) {
            cache().remove(key);
        } else {
            cache().put(key, value);
            regulateSize();
        }
        return value;
    }
}
