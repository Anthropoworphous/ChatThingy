package com.github.anthropoworphous.chatthingy.user.impl.readonly;

import com.github.anthropoworphous.chatthingy.error.handling.ExceptionHandler;
import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.log.Logger;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.adaptor.MessageAdaptor;
import com.github.anthropoworphous.chatthingy.msg.elements.Elements;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.User;
import discord4j.common.util.Snowflake;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * It's not individual user but a channel because how the chat works
 */
public class DiscordChannelUser extends User<MessageCreateSpec.Builder> {
    private final Channel channel;

    public DiscordChannelUser(String channelId) {
        super(channelId);
        this.channel = DiscordHook.client()
                .map(client -> client.getChannelById(Snowflake.of(channelId))
                        .block(Duration.ofSeconds(10)))
                .orElse(null);
    }
    public DiscordChannelUser(Channel channel) {
        super(channel.getId().asString());
        this.channel = channel;
    }

    @Override
    protected void accept(MessageCreateSpec.Builder content) {
        Optional.ofNullable(channel)
                .ifPresent(c -> c.getRestChannel()
                        .createMessage(content.build().asRequest())
                        .subscribe());
    }

    @Override
    public MessageCreateSpec.Builder read(IMessage message) {
        return MessageCreateSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .author(message.sender().name().orElse("Anonymous"), null, message.sender().pfpUrl())
                        .description(new MessageAdaptor(message)
                                .readString(ep -> new IElement[] {Elements.message()})
                        ).build());
    }

    @Override
    public MessageCreateSpec.Builder error(Exception e) {
        try (StringWriter sw = new StringWriter()) {
            new ExceptionHandler(new Logger(sw), new ExceptionHandler.StackTraceCutter()).handle(e);
            return MessageCreateSpec.builder()
                    .addEmbed(EmbedCreateSpec.builder()
                            .author("Error", null, "https://mc-heads.net/avatar/false")
                            .addField(e.toString(), "Damn...", false)
                            .build())
                    .addFile("error.txt", new ByteArrayInputStream(sw.toString().getBytes()))
                    .content(e.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public MessageCreateSpec.Builder acceptButton(List<Button> buttons) {
        return MessageCreateSpec.builder()
                .content("Received buttons:")
                .addComponent(ActionRow.of(
                    buttons.stream()
                            .map(b -> discord4j.core.object.component.Button.primary(
                                    b.trigger().trigger(),
                                    b.name()
                            )).toList()
                ));
    }

    @Override
    public boolean checkPermission(String node) {
        return Optional.ofNullable(DiscordHook.configOfChannel(id()))
                .map(conf -> conf.checkPerm(node))
                .orElse(false);
    }
}
