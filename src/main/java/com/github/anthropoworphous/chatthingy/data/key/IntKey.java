package com.github.anthropoworphous.chatthingy.data.key;

import com.github.anthropoworphous.chatthingy.data.Key;
import org.jetbrains.annotations.NotNull;

public record IntKey(int i) implements Key<IntKey> {
    @Override
    public int compareTo(@NotNull IntKey o) { return i - o.i; }
}
