package com.github.anthropoworphous.chatthingy.user;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface User {
    // user's general descriptions
    @NotNull
    String name();

    // user's detail descriptions
    default Optional<String> title() { return Optional.empty(); }
    default Optional<String> prefix()  { return Optional.empty(); }
    default Optional<String> postfix()  { return Optional.empty(); }

    // user's full descriptions
    default Optional<List<String>> bio() { return Optional.empty(); }
    default Optional<List<String>> ranks() { return Optional.empty(); }
    default Optional<List<String>> descriptions() { return Optional.empty(); }
}
