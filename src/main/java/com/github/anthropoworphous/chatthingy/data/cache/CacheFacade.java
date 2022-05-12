package com.github.anthropoworphous.chatthingy.data.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface CacheFacade<K, V> extends Cache<K, V> {
    Cache<K, V> cache();

    @Override
    default V cache(K key, Function<K, V> mapper) {
        return cache().cache(key, mapper);
    }

    @Override
    default int limit() {
        return cache().limit();
    }

    @Override
    default void limit(int limit) {
        cache().limit();
    }

    @Override
    default K removedTarget() {
        return cache().removedTarget();
    }

    @Override
    default int size() {
        return cache().size();
    }

    @Override
    default boolean isEmpty() {
        return cache().isEmpty();
    }

    @Override
    default boolean containsKey(Object key) {
        return cache().containsKey(key);
    }

    @Override
    default boolean containsValue(Object value) {
        return cache().containsValue(value);
    }

    @Override
    default V get(Object key) {
        return cache().get(key);
    }

    @Nullable
    @Override
    default V put(K key, V value) {
        return cache().put(key, value);
    }

    @Override
    default V remove(Object key) {
        return cache().remove(key);
    }

    @Override
    default void putAll(@NotNull Map<? extends K, ? extends V> m) {
        cache().putAll(m);
    }

    @Override
    default void clear() {
        cache().clear();
    }

    @NotNull
    @Override
    default Set<K> keySet() {
        return cache().keySet();
    }

    @NotNull
    @Override
    default Collection<V> values() {
        return cache().values();
    }

    @NotNull
    @Override
    default Set<Entry<K, V>> entrySet() {
        return cache().entrySet();
    }
}
