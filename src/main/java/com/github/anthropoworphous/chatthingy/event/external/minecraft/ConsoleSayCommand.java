package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.impl.SendToPrivateChat;
import com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.impl.SendToStaffChat;
import com.github.anthropoworphous.chatthingy.msg.message.Message;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.group.LinkedDiscordChannels;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ConsoleSayCommand implements Listener, Event {
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if (!event.getCommand().contains("say")) { return; }
        event.setCancelled(true);

        User<String> consoleUser = new ConsoleUser();

        new Message.Builder()
                .sendBy(consoleUser)
                .content(event.getCommand().substring(4)) // skip the "/say "
                .readBy(new ReaderCollector(new OnlinePlayerReaders())
                        .with(consoleUser)
                        .withAllOf(new LinkedDiscordChannels()))
                .interceptors(
                        new SendToPrivateChat(),
                        new SendToStaffChat())
                .build().task().run();
    }

    @Override
    public String eventName() {
        return "ConsoleSayCommandEvent";
    }
}
