package com.github.anthropoworphous.chatthingy.event;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.text.impl.StringText;
import com.github.anthropoworphous.chatthingy.task.SendTask;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.reader.impl.OnlinePlayersReaders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ConsoleSayCommand implements Listener {
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if (!event.getCommand().contains("say")) { return; }
        event.setCancelled(true);

        new SendTask(
                new Message(
                        new ConsoleUser(),
                        new StringText(event.getCommand().substring(4)), // skip the "/say "
                        new OnlinePlayersReaders()
                )
        ).run();
    }
}
