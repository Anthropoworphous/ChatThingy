package com.github.anthropoworphous.chatthingy.data.config;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.channel.impl.General;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Private;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Staff;
import com.github.anthropoworphous.chatthingy.data.config.channel.ChannelConfig;
import com.github.anthropoworphous.chatthingy.data.config.event.EventConfig;
import com.github.anthropoworphous.chatthingy.event.external.minecraft.PlayerJoinLeave;

import java.nio.file.Path;

public class ConfigManager {
    public static void reload() {
        ChannelConfig.reload(General.channel());
        ChannelConfig.reload(Private.channel());
        ChannelConfig.reload(Staff.channel());

        EventConfig.reload(PlayerJoinLeave.get());

        // HookConfig.reload();
    }

    public static Path getDataFolder() {
        return ChatThingy.plugin().getDataFolder().toPath().resolve("config");
    }
}
