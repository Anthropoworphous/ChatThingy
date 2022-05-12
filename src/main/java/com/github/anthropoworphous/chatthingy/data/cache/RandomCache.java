package com.github.anthropoworphous.chatthingy.data.cache;

import java.io.Serializable;
import java.util.HashMap;

public class RandomCache<K, T> extends AbstractCache<K, T> implements Serializable {
    public RandomCache(int sizeLimit) {
        super(new HashMap<>(), sizeLimit);
    }
    public RandomCache() {
        this(-1);
    }

    @Override
    public K removedTarget() {
        return cache().keySet()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Empty list attempted to remove extra"));
    }
}
