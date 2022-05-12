package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;

import java.util.Optional;

public class NamedEmptyUser extends EmptyUser {
    private final String name;

    public NamedEmptyUser(String name) {
        this.name = name;
    }

    @Override
    public Optional<String> name() {
        return Optional.of(name);
    }
}
