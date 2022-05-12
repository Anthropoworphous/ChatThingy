package com.github.anthropoworphous.chatthingy.channel.impl.minecraft;

import com.github.anthropoworphous.chatthingy.channel.Channel;

import java.util.Optional;

public class Staff implements Channel {
    private static final Channel instance = new Staff();

    public static Channel channel() { return instance; }

    @Override
    public Optional<String> name() {
        return Optional.of("Staff");
    }

    @Override
    public String defaultReadPerm() { return "chat_thingy.staff_read"; }
    @Override
    public String defaultSendPerm() { return "chat_thingy.staff_send"; }

    @Override
    public Optional<Character> trigger() {
        return Optional.of('!');
    }
}
