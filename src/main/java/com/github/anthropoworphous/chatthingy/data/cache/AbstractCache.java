package com.github.anthropoworphous.chatthingy.data.cache;

import com.github.anthropoworphous.chatthingy.util.ThrowableFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    private final Map<K, V> cache;
    private int limit;

    protected AbstractCache(Map<K, V> cache, int limit) {
        this.cache = cache;
        this.limit = limit;
    }

    // getter
    @Override
    public Map<K, V> cache() {
        return cache;
    }
    @Override
    public final int limit() {
        return limit;
    }

    // setter
    @Override
    public void limit(int limit) {
        this.limit = limit;
        regulateSize();
    }

    // from cache
    public void possibleErrorCompute(K key, ThrowableFunction<V, V> mapper) throws Exception {
        if (cache().containsKey(key)) {
            put(key, mapper.map(cache().get(key)).orElseThrow());
        } else {
            throw new NullPointerException("No value associated with the key");
        }
    }
    public void putIfAbsentOrPossibleErrorCompute(
            K key,
            V defaultValue,
            ThrowableFunction<V, V> mapper
    ) throws Exception {
        putIfAbsent(key, defaultValue);
        possibleErrorCompute(key, mapper);
    }

    // facade below this point
    // view
    @Override
    public int size() { return cache.size(); }
    @Override
    public Set<Map.Entry<K, V>> entrySet() { return cache.entrySet(); }
    @Override
    public Set<K> keySet() { return cache.keySet(); }
    @Override
    public Collection<V> values() { return cache.values(); }
    @Override
    public boolean isEmpty() { return cache.isEmpty(); }
    @Override
    public boolean containsKey(Object key) { return cache.containsKey(key); }
    @Override
    public boolean containsValue(Object value) { return cache.containsValue(value); }
    @Override
    public V get(Object key) { return cache.get(key); }
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) { cache.forEach(action);}

    // edit
    @Override
    public void clear() { cache.clear(); }
    @Override
    public V remove(Object key) { return cache.remove(key); }
    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        cache.putAll(m);
        regulateSize();
    }
    @Override
    public V putIfAbsent(K key, V defaultValue) {
        return cache.putIfAbsent(key, defaultValue);
    }
    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mapper) {
        return cache().computeIfAbsent(key, mapper);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return Cache.super.getOrDefault(key, defaultValue);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Cache.super.replaceAll(function);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return Cache.super.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return Cache.super.replace(key, oldValue, newValue);
    }

    @Nullable
    @Override
    public V replace(K key, V value) {
        return Cache.super.replace(key, value);
    }

    @Override
    public @Nullable V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Cache.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Cache.super.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return Cache.super.merge(key, value, remappingFunction);
    }
}
