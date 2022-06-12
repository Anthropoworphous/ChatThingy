package com.github.anthropoworphous.chatthingy.event.external.minecraft;

import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter.ExpendSlang;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter.SpamFilter;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.minecraft.HighLightMCName;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl.SendToPrivateChat;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl.SendToStaffChat;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.group.LinkedDiscordChannels;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class InGameChat implements Listener, Event {
    private static final InGameChat instance = new InGameChat();

    private InGameChat() {}

    public static InGameChat get() { return instance; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInGameChat(AsyncChatEvent event) {
        // if it's send by player it's always asynchronous
        if (!event.isAsynchronous()) {
            return;
        }

        // getChannelConfig out of here old minecraft chat
        event.setCancelled(true);

        new SendTask(
                new Message(
                        new PlayerUser(event.getPlayer()),
                        event.message(),
                        new ReaderCollector(new OnlinePlayerReaders())
                                .with(new ConsoleUser())
                                .withAllOf(new LinkedDiscordChannels())
                ),
                new SendToPrivateChat(),
                new SendToStaffChat(),
                new HighLightMCName(),
                new ExpendSlang(),
                new SpamFilter()
        ).run();
    }

    @Override
    public String eventName() {
        return "InGameChatEvent";
    }
}
