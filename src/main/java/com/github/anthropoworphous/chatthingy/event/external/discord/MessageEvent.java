package com.github.anthropoworphous.chatthingy.event.external.discord;

import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
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

public class MessageEvent implements DiscordEvent {
    private final DiscordHook hookInstance;

    public MessageEvent(DiscordHook hookInstance) {
        this.hookInstance = hookInstance;
    }

    public void init(GatewayDiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class, event -> {
            // stop processing if the user isn't a normal discord user
            if (event.getMember().isEmpty() || event.getMember().get().isBot()) { return Mono.empty(); }

            if (event.getMessage().getContent().startsWith(hookInstance.commandPrefix() + " ")) {
                return event.getMessage()
                        .getChannel()
                        .flatMap(c -> c.createMessage(
                                switch (CommandProcessor.dispatch(event, hookInstance)) {
                                    case -1 -> "Not ready yet";
                                    case -2 -> "Unknown command";
                                    case -3 -> "Invalid input";
                                    default -> "OK";
                                }
                        ));
            }

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
