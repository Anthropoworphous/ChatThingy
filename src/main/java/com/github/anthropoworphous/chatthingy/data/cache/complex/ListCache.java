package com.github.anthropoworphous.chatthingy.data.cache.complex;

import com.github.anthropoworphous.chatthingy.data.cache.Cache;
import com.github.anthropoworphous.chatthingy.data.cache.CacheFacade;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class ListCache<K, V, L extends Collection<V>> implements CacheFacade<K, L> {
    private final Cache<K, L> cache;
    private final Supplier<L> listSupplier;

    public ListCache(Supplier<Cache<K, L>> cache, Supplier<L> listSupplier, int limit) {
        this.cache = cache.get();
        this.cache.limit(limit);
        this.listSupplier = listSupplier;
    }
    public ListCache(Supplier<Cache<K, L>> cache, Supplier<L> listSupplier) {
        this(cache, listSupplier, -1);
    }

    public void add(K key, V value) {
        cache.cache(key, k -> listSupplier.get()).add(value);
        regulateSize();
    }
    public Collection<V> cache(K key) {
        return cache(key, k -> listSupplier.get());
    }
    public Optional<Entry<K, L>> largest() {
        int max = 0;
        Entry<K, L> largest = null;
        for (Entry<K, L> entry : cache.entrySet()) {
            int newMax = entry.getValue().size();
            if (largest == null || max < newMax) {
                max = newMax;
                largest = entry;
            }
        }
        return Optional.ofNullable(largest);
    }

    @SuppressWarnings("uncheck")
    public static <K, V, L extends Collection<V>> ListCache<K, V, L> from(Cache<K, L> c) {
        return (ListCache<K, V, L>) c;
    }

    @Override
    public Cache<K, L> cache() {
        return cache;
    }

    @Override
    public K removedTarget() {
        return entrySet().stream()
                .filter(e -> e.getValue().size() == 0)
                .map(Entry::getKey)
                .findFirst()
                .orElse(CacheFacade.super.removedTarget());
    }
}
