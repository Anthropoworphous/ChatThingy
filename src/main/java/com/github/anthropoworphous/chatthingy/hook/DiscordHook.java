package com.github.anthropoworphous.chatthingy.hook;

import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.event.external.discord.ButtonPressedEvent;
import com.github.anthropoworphous.chatthingy.event.external.discord.MessageEvent;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.bukkit.Bukkit;
import org.ini4j.Ini;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public class DiscordHook extends Configured implements Hook {
    private static Instant up = null;
    private static GatewayDiscordClient client = null;

    @Override
    public String hookName() {
        return "discord-hook";
    }

    @Override
    public void init() {
        String strToken = get("connection",
                "token",
                "0000000000000000000000000000000000000000000000000000000000000000000000");
        if (strToken.equals("0000000000000000000000000000000000000000000000000000000000000000000000")) {
            return;
        }

        try {
            DiscordClient.create(strToken).login().doOnSuccess(client -> {
                up = new Date().toInstant();

                DiscordHook.client = client;
                MessageEvent.init(client);
                ButtonPressedEvent.init(client);
            }).subscribe();
        } catch(Exception e) {
            Bukkit.getLogger().info("Failed to connect to discord: %s".formatted(e.getMessage()));
        }
    }

    // resource getter
    public static Optional<GatewayDiscordClient> client() {
        return Optional.ofNullable(client);
    }

    public static String upTime() {
        if (up != null) {
            long seconds = up.until(new Date().toInstant(), ChronoUnit.SECONDS);
            return "%d days".formatted((int) (seconds / 86400)) +
                    ", %d hours".formatted((int) (seconds / 3600 % 24)) +
                    ", %d minutes".formatted((int) (seconds / 60 % 60)) +
                    ", %d seconds".formatted((int) (seconds % 60));
        }
        return "Not up yet";
    }

    @Override
    protected String configFileName() {
        return "discord";
    }

    @Override
    protected File configFolder() {
        return new File(CONFIG_FOLDER, "hooks");
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();
        ini.put("connection", "token",
                "0000000000000000000000000000000000000000000000000000000000000000000000");
        return ini;
    }
}
