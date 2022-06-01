package com.github.anthropoworphous.chatthingy.hook;

import com.github.anthropoworphous.chatthingy.data.config.hook.HookConfig;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.bukkit.Bukkit;

import java.util.Optional;

public class DiscordHook implements Hook {
    private static GatewayDiscordClient client = null;

    @Override
    public String hookName() {
        return "discord-hook";
    }

    @Override
    public void init() {
        Optional<String> strToken = HookConfig.load(this)
                .map(c -> c.get("connection", "token"));
        if (strToken.isEmpty()) { return; }

        try {
            DiscordClient.create(strToken.get()).login().doOnSuccess(client -> DiscordHook.client = client)
                    .subscribe();
        } catch(Exception e) {
            Bukkit.getLogger().info("Failed to connect to discord: %s".formatted(e.getMessage()));
        }
    }

    // resource getter
    public static Optional<GatewayDiscordClient> client() {
        return Optional.ofNullable(client);
    }
}
