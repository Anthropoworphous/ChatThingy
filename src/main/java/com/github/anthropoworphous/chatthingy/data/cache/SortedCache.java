package com.github.anthropoworphous.chatthingy.data.cache;

import java.io.Serializable;
import java.util.TreeMap;

public class SortedCache<K extends Comparable<K>, T> extends AbstractCache<K, T> implements Serializable {
    public SortedCache(int sizeLimit) {
        super(new TreeMap<>(), sizeLimit);
    }
    public SortedCache() {
        this(-1);
    }

    @Override
    public K removedTarget() { return cache().lastKey(); }

    @Override
    public TreeMap<K, T> cache() { return (TreeMap<K, T>) super.cache(); }
}
