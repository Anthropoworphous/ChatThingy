package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;

import java.util.Optional;

public class NamedEmptyUser<T> extends EmptyUser<T> {
    private final String name;

    public NamedEmptyUser(String name) {
        this.name = name;
    }

    @Override
    public Optional<String> name() {
        return Optional.of(name);
    }
}
