package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl.SendToPrivateChat;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl.SendToStaffChat;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.impl.readonly.DiscordChannelUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ConsoleSayCommand implements Listener, Event {
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if (!event.getCommand().contains("say")) { return; }
        event.setCancelled(true);

        User<String> consoleUser = new ConsoleUser();

        new SendTask(
                new Message(
                        consoleUser,
                        event.getCommand().substring(4), // skip the "/say "
                        new ReaderCollector(new OnlinePlayerReaders())
                                .with(consoleUser)
                                .with(new DiscordChannelUser("970015392296742952"))
                ),
                new SendToPrivateChat(),
                new SendToStaffChat()
        ).run();
    }

    @Override
    public String eventName() {
        return "ConsoleSayCommandEvent";
    }
}
