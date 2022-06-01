package com.github.anthropoworphous.chatthingy.data.cache;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache<K, V> extends AbstractCache<K, V> {
    public ConcurrentCache(int limit) {
        super(new ConcurrentHashMap<>(), limit);
    }

    public ConcurrentCache() {
        this(-1);
    }

    @Override
    public K removedTarget() {
        return cache().keySet().stream().findAny().orElse(null);
    }
}
