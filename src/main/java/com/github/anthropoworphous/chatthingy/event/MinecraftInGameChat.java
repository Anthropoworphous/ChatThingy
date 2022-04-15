package com.github.anthropoworphous.chatthingy.event;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.text.impl.AdventureText;
import com.github.anthropoworphous.chatthingy.task.SendTask;
import com.github.anthropoworphous.chatthingy.task.interceptor.mc.HighLightMCName;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import com.github.anthropoworphous.chatthingy.user.reader.impl.OnlinePlayersReaders;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MinecraftInGameChat implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInGameChat(AsyncChatEvent event) {
        // if it's send by player it's always asynchronous
        if (!event.isAsynchronous()) {
            return;
        }

        // get out of here old minecraft chat
        event.setCancelled(true);

        new SendTask(
                new Message(
                        new PlayerUser(event.getPlayer()),
                        new AdventureText(event.message()),
                        new OnlinePlayersReaders()
                ),
                new HighLightMCName()
        ).run();
    }
}
