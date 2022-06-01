package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import com.github.anthropoworphous.chatthingy.user.impl.sendonly.DiscordChannelUser;
import com.github.anthropoworphous.chatthingy.util.ColorCodeDecoder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ini4j.Ini;

import java.io.File;

public class PlayerJoinLeave extends Configured implements Listener, Event {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        new SendTask(new Message(
                new EmptyUser(),
                ColorCodeDecoder.decode(get("text", "join-message", "&a[+]&r <p>")
                        .replaceAll("<p>", event.getPlayer().getName())),
                new ReaderCollector(new OnlinePlayerReaders())
                        .with(new DiscordChannelUser("970015392296742952"))
        )).run();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.quitMessage(null);

        new SendTask(new Message(
                new EmptyUser(),
                ColorCodeDecoder.decode(get("text", "leave-message", "&c[-]&r <p>")
                        .replaceAll("<p>", event.getPlayer().getName())),
                new ReaderCollector(new OnlinePlayerReaders())
                        .with(new DiscordChannelUser("970015392296742952"))
        )).run();
    }

    @Override
    public String eventName() {
        return "PlayerJoinLeaveEvent";
    }

    @Override
    protected String configFileName() {
        return getClass().getSimpleName();
    }

    @Override
    protected File configFolder() {
        return new File(CONFIG_FOLDER, "event" + File.separator + "minecraft");
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();
        ini.putComment("text",
                "<p> will be replaced by name of player, " +
                        "use color code similar to skript, hex looks like <#000000>");
        ini.put("text", "join-message", "&a[+]&r <p>");
        ini.put("text", "leave-message", "&c[-]&r <p>");
        return ini;
    }
}
