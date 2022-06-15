package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.data.config.BukkitConfiguration;
import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.message.Message;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.group.LinkedDiscordChannels;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import com.github.anthropoworphous.chatthingy.util.ColorCodeDecoder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ini4j.Ini;

import java.io.File;

public class PlayerJoinLeave implements Listener, Event {
    private final static BukkitConfiguration config = new BukkitConfiguration.Builder()
            .name("PlayerJoinLeave")
            .folder(f -> new File(f, "event%sminecraft".formatted(File.separator)))
            .defaultIniCreator(() -> {
                Ini ini = new Ini();
                ini.putComment("text",
                        "<p> will be replaced by name of player, " +
                                "use color code similar to skript, hex looks like <#000000>");
                ini.put("text", "join-message", "&a[+]&r <p>");
                ini.put("text", "leave-message", "&c[-]&r <p>");
                return ini;
            }).build();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        new Message.Builder()
                .sendBy(new EmptyUser())
                .content(ColorCodeDecoder.decode(config.get("text", "join-message")
                                .replaceAll("<p>", event.getPlayer().getName())))
                .readBy(new ReaderCollector(new OnlinePlayerReaders())
                        .withAllOf(new LinkedDiscordChannels()))
                .build().task().run();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.quitMessage(null);

        new Message.Builder()
                .sendBy(new EmptyUser())
                .content(ColorCodeDecoder.decode(config.get("text", "leave-message")
                        .replaceAll("<p>", event.getPlayer().getName())))
                .readBy(new ReaderCollector(new OnlinePlayerReaders())
                        .withAllOf(new LinkedDiscordChannels()))
                .build().task().run();
    }

    @Override
    public String eventName() {
        return "PlayerJoinLeaveEvent";
    }
}
