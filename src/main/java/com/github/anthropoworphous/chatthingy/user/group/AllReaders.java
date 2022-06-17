package com.github.anthropoworphous.chatthingy.user.group;

import com.github.anthropoworphous.chatthingy.user.User;

import java.util.ArrayList;

public class AllReaders extends ArrayList<User<?>> {
    public AllReaders() {
        super(new LinkedDiscordChannels());
        addAll(new OnlinePlayerReaders());
    }
}
