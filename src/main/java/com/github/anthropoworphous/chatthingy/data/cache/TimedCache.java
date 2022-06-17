package com.github.anthropoworphous.chatthingy.data.cache;

import com.github.anthropoworphous.chatthingy.data.key.TimeKey;

import java.util.Collection;

public class TimedCache<V> extends SortedCache<TimeKey, V> {
    public Collection<V> get() { return values(); }

    public static <V> TimedCache<V> from(Cache<TimeKey, V> c) { return (TimedCache<V>) c; }

    public void update(TimeKey key) {
        put(new TimeKey(), remove(key));
    }
}