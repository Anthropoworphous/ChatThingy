package com.github.anthropoworphous.chatthingy.data.key;

import com.github.anthropoworphous.chatthingy.data.Key;
import org.jetbrains.annotations.NotNull;

public record StringKey(String str) implements Key<StringKey> {
    @Override
    public int compareTo(@NotNull StringKey o) { return str.compareTo(o.str); }
}
