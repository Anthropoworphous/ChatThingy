package com.github.anthropoworphous.chatthingy.data.key;

import com.github.anthropoworphous.chatthingy.data.Key;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Date;

public record TimeKey(Instant t) implements Key<TimeKey> {
    public TimeKey() {
        this(new Date().toInstant());
    }

    @Override
    public int compareTo(@NotNull TimeKey o) { return t.compareTo(o.t); }
}
