package com.github.anthropoworphous.chatthingy.channel.impl.minecraft;

import com.github.anthropoworphous.chatthingy.channel.Channel;

import java.util.Optional;

public class Private implements Channel {
    private static final Channel instance = new Private();

    public static Channel channel() { return instance; }

    @Override
    public Optional<String> name() {
        return Optional.of("Private");
    }

    @Override
    public String defaultReadPerm() { return "null"; }
    @Override
    public String defaultSendPerm() { return "null"; }

    @Override
    public Optional<Character> trigger() {
        return Optional.of('@');
    }
}
