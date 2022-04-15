package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConsoleUser implements User {
    @Override
    public @NotNull String name() {
        return "console";
    }

    @Override
    public Optional<String> prefix() {
        return Optional.of("[Console]");
    }

    @Override
    public Optional<List<String>> bio() {
        return Optional.of(List.of("Logging ip since 2019"));
    }

    @Override
    public Optional<List<String>> descriptions() {
        return Optional.of(List.of("Just a console"));
    }
}
