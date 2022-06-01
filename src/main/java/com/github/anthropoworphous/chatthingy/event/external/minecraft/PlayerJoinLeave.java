package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.data.config.event.EventConfig;
import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

public class PlayerJoinLeave implements Listener, Event {
    private static final PlayerJoinLeave instance = new PlayerJoinLeave();

    private PlayerJoinLeave() {}

    public static PlayerJoinLeave get() { return instance; }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        TextColor colorConfig = EventConfig.loadOr(
                this,
                c -> c.parse("color", "join-color", str -> {
                    String[] list = str.split(",");
                    return strToColor(list[0], list[1], list[2]);
                }), NamedTextColor.GREEN
        );

        new SendTask(new Message(
                new EmptyUser(),
                Component.text(EventConfig.loadOr(
                        this, c -> c.get("text", "join-message"), "[+] <p>")
                                .replaceAll("<p>", event.getPlayer().getName())
                ).color(colorConfig),
                new ReaderCollector(new OnlinePlayerReaders())
        )).run();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.quitMessage(null);

        TextColor colorConfig = EventConfig.loadOr(
                this,
                c -> c.parse("color", "leave-color", str -> {
                    String[] list = str.split(",");
                    return strToColor(list[0], list[1], list[2]);
                }), NamedTextColor.RED
        );

        new SendTask(new Message(
                new EmptyUser(),
                Component.text(EventConfig.loadOr(
                                this, c -> c.get("text", "leave-message"), "[-] <p>")
                        .replaceAll("<p>", event.getPlayer().getName())
                ).color(colorConfig),
                new ReaderCollector(new OnlinePlayerReaders())
        )).run();
    }

    @Override
    public String eventName() {
        return "PlayerJoinLeaveEvent";
    }

    private @Nullable TextColor strToColor(String str1, String str2, String str3) {
        try {
            return TextColor.color(Integer.parseInt(str1), Integer.parseInt(str2), Integer.parseInt(str3));
        } catch (Exception e) {
            return null;
        }
    }
}
