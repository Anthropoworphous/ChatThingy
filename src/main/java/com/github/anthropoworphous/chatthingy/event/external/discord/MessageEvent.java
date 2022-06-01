package com.github.anthropoworphous.chatthingy.event.external.discord;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter.AutoCaps;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter.ExpendSlang;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter.SpamFilter;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.extend.NamedEmptyUser;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class MessageEvent {
    public static void init(GatewayDiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class, event -> {
            if (event.getMember().isEmpty() || event.getMember().get().isBot()) { return Mono.empty(); }
            new SendTask(
                    new Message(
                            event.getMember()
                                    .map(m -> (User<Object>) new NamedEmptyUser(m.getUsername()))
                                    .orElse(new EmptyUser()),
                            event.getMessage().getContent(),
                            new ReaderCollector(new OnlinePlayerReaders())),
                    new ExpendSlang(),
                    new AutoCaps(),
                    new SpamFilter()
            ).run();
            return Mono.empty();
        }).subscribe();
    }
}
