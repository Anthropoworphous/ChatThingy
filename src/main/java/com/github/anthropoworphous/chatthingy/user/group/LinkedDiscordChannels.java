package com.github.anthropoworphous.chatthingy.user.group;

import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.user.impl.readonly.DiscordChannelUser;

import java.util.ArrayList;

public class LinkedDiscordChannels extends ArrayList<DiscordChannelUser> {
    public LinkedDiscordChannels() {
        super(DiscordHook.linkedChannels().stream().map(k -> new DiscordChannelUser(k.str())).toList());
    }
}
