package com.github.anthropoworphous.chatthingy.data.cache.complex;

import com.github.anthropoworphous.chatthingy.data.cache.Cache;
import com.github.anthropoworphous.chatthingy.data.cache.CacheFacade;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapCache<K, KK, VV> implements CacheFacade<K, Cache<KK, VV>> {
    private final Cache<K, Cache<KK, VV>> cache;
    private final Function<K, Cache<KK, VV>> mapper;

    public MapCache(int limit, Supplier<Cache<K, Cache<KK, VV>>> cache, Function<K, Cache<KK, VV>> mapper) {
        this.cache = cache.get();
        this.cache.limit(limit);
        this.mapper = mapper;
    }
    public MapCache(Supplier<Cache<K, Cache<KK, VV>>> cache, Function<K, Cache<KK, VV>> mapper) {
        this.cache = cache.get();
        this.cache.limit(-1);
        this.mapper = mapper;
    }

    public VV getNested(K key1, KK key2) { return cache.cache(key1, mapper).get(key2); }
    public VV cacheNested(K key1, KK key2, BiFunction<K, KK, VV> value) { return cache.cache(key1, mapper).get(key2); }
    public @Nullable VV removeNested(K key1, KK key2) {
        return Optional.ofNullable(get(key1)).map(c -> c.remove(key2)).orElse(null);
    }
    public void putNested(K key1, KK key2, VV value) {
        cache.cache(key1, mapper).put(key2, value);
    }

    // avoiding problem
    public static <K, KK, VV> MapCache<K, KK, VV> from(Cache<K, Cache<KK, VV>> c) {
        return (MapCache<K, KK, VV>) c;
    }

    @Override
    public Cache<K, Cache<KK, VV>> cache() { return cache; }

    @Override
    public K removedTarget() {
        return entrySet().stream()
                .filter(e -> e.getValue().size() == 0)
                .map(Entry::getKey)
                .findFirst()
                .orElse(CacheFacade.super.removedTarget());
    }
}
