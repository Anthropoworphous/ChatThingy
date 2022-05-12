package com.github.anthropoworphous.chatthingy.channel.impl;

import com.github.anthropoworphous.chatthingy.channel.Channel;

import java.util.Optional;

public class General implements Channel {
    private static final Channel instance = new General();

    public static Channel channel() { return instance; }

    @Override
    public Optional<String> name() {
        return Optional.empty();
    }

    @Override
    public String defaultReadPerm() { return "null"; }
    @Override
    public String defaultSendPerm() { return "null"; }

    @Override
    public Optional<Character> trigger() {
        return Optional.empty();
    }
}
