package com.github.anthropoworphous.chatthingy.event.external.discord;

import com.github.anthropoworphous.chatthingy.cmd.discord.CommandProcessor;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.msg.interceptor.formatter.AutoCaps;
import com.github.anthropoworphous.chatthingy.msg.interceptor.formatter.ExpendSlang;
import com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.impl.SendToPrivateChat;
import com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.impl.SendToStaffChat;
import com.github.anthropoworphous.chatthingy.msg.message.Message;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.extend.DiscordMemberUser;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

public class MessageEvent implements DiscordEvent {
    private static final String[] discordCommandErrorRespawns = { "Not ready yet", "Unknown command", "Invalid input" };
    private final DiscordHook hookInstance;

    public MessageEvent(DiscordHook hookInstance) {
        this.hookInstance = hookInstance;
    }

    public void init(GatewayDiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class, event -> {
            // stop processing if the user isn't a normal discord user
            if (event.getMember().isEmpty() || event.getMember().get().isBot()) { return Mono.empty(); }

            // also stop when the message is empty
            if (event.getMessage().getContent().length() == 0) { return Mono.empty(); }

            // checking if the message starts with the command prefix, and if it does, it will run the command.
            if (event.getMessage().getContent().startsWith(hookInstance.commandPrefix() + " ")) {
                int result = CommandProcessor.dispatch(event, hookInstance);
                if (result >= 0) {
                    return event.getMessage().getRestMessage().createReaction("👍");
                }
                return event.getMessage()
                        .getChannel()
                        .flatMap(c -> c.createMessage(
                                discordCommandErrorRespawns[-result]
                        )).then();
            }

            MessageChannel channel = event.getMessage().getChannel().block(Duration.ofSeconds(10));
            if (channel == null || !DiscordHook.linkedChannels().contains(new StringKey(channel.getId().asString()))) {
                return Mono.empty();
            }
            DiscordHook.ChannelConfig config = DiscordHook.configOfChannel(channel.getId().toString());
            String prefix = Optional.ofNullable(config)
                    .map(channelConfig -> channelConfig.messagePrefix() + " ")
                    .orElse("");

            return Mono.fromRunnable(new Message.Builder()
                    .sendBy(event.getMember()
                            .map(m -> (User<?>) new DiscordMemberUser(channel, m))
                            .orElse(new EmptyUser<>()))
                    .content(prefix + event.getMessage().getContent())
                    .readBy(new ReaderCollector(new ConsoleUser()))
                    .interceptors(
                            new SendToPrivateChat(),
                            new SendToStaffChat(),
                            new ExpendSlang(),
                            new AutoCaps())
                    .build().task()
            );
        }).subscribe();
    }
}
