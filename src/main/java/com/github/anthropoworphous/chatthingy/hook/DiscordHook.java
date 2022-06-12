package com.github.anthropoworphous.chatthingy.hook;

import com.github.anthropoworphous.chatthingy.data.cache.ConcurrentCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.PersistentCache;
import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.event.external.discord.ButtonPressedEvent;
import com.github.anthropoworphous.chatthingy.event.external.discord.CommandProcessor;
import com.github.anthropoworphous.chatthingy.event.external.discord.MessageEvent;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.bukkit.Bukkit;
import org.ini4j.Ini;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class DiscordHook extends Configured implements Hook {
    private final Supplier<String> commandPrefix = () -> get("command", "prefix");

    private static final PersistentCache<StringKey, ChannelConfig> connectedChannels = new PersistentCache<>(
            "ConnectedDiscordChannels", ConcurrentCache::new
    );
    private static Instant up = null; // for uptime
    private static GatewayDiscordClient client = null;

    @Override
    public String hookName() {
        return "discord-hook";
    }

    @Override
    public void init() {
        String strToken = get("connection", "token");
        if (strToken.equals("0".repeat(70))) {
            Bukkit.getLogger().info("You can set the token in %s to connect to discord channel through a bot"
                    .formatted(configFolder().getAbsolutePath()));
            return;
        }

        // try to connect to discord.
        try {
            DiscordClient.create(strToken).login().doOnSuccess(client -> {
                // Setting the uptime, setting the client, and initializing the events.
                up = new Date().toInstant();
                DiscordHook.client = client;
                new CommandProcessor().init(client);
                new MessageEvent(this).init(client);
                new ButtonPressedEvent().init(client);
            }).subscribe();
        } catch(Exception e) {
            Bukkit.getLogger().info("Failed to connect to discord: %s".formatted(e.getMessage()));
        }
    }

    // resource getter and setter
    public PersistentCache<StringKey, ChannelConfig> connectedChannels() {
        return connectedChannels;
    }
    public String commandPrefix() { return commandPrefix.get(); }
    public void linkChannel(String id) {
        var config = new ChannelConfig(id);
        connectedChannels().put(new StringKey(id), config);
    }
    public void unlinkChannel(String id) {
        connectedChannels().remove(new StringKey(id));
    }

    // resource getter - static
    public static Optional<GatewayDiscordClient> client() { return Optional.ofNullable(client); }
    public static Set<StringKey> linkedChannels() { return connectedChannels.keySet(); }
    public static @Nullable ChannelConfig configOfChannel(String id) { return connectedChannels.get(new StringKey(id)); }
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

    // configuration
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
        ini.put("command", "prefix", "ct");
        ini.put("connection", "token", "0".repeat(70));
        return ini;
    }

    // individual channel config
    public static class ChannelConfig extends Configured implements Serializable {
        private final String id;
        public ChannelConfig(@NotNull String channelId) {
            id = channelId;
            reload();
        }

        public boolean checkPerm(String node) {
            return Arrays.asList(get("data", "permission").split(",")).contains(node);
        }
        public String messagePrefix() { return get("data", "message prefix"); }

        @Override
        protected String configFileName() {
            return id;
        }

        @Override
        protected File configFolder() {
            return new File(CONFIG_FOLDER, "hooks%schannels".formatted(File.separator));
        }

        @Override
        protected Ini defaultIni() {
            Ini ini = new Ini();
            ini.putComment("data", "permission: separate permissions with \",\", this is the permission this channel will have");
            ini.putComment("data", "message prefix: this will get added before the message");
            ini.put("data", "permission", "example.perm_1,example.perm_2");
            ini.put("data", "message prefix", "");
            return ini;
        }
    }
}
