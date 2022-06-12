package com.github.anthropoworphous.chatthingy.user.impl.readonly;

import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.adaptor.MsgAdaptor;
import com.github.anthropoworphous.chatthingy.msg.elements.Elements;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.user.User;
import discord4j.common.util.Snowflake;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.util.MultipartRequest;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * It's not individual user but a channel because how the chat works
 */
public class DiscordChannelUser extends User<MultipartRequest<MessageCreateRequest>> {
    private final Channel channel;

    public DiscordChannelUser(String channelId) {
        super(channelId);
        this.channel = DiscordHook.client()
                .map(client -> client.getChannelById(Snowflake.of(channelId))
                        .block(Duration.ofSeconds(10)))
                .orElse(null);
    }

    @Override
    protected void accept(MultipartRequest<MessageCreateRequest> content) {
        Optional.ofNullable(channel).ifPresent(c -> c.getRestChannel().createMessage(content).subscribe());
    }

    @Override
    public MultipartRequest<MessageCreateRequest> read(Message message) {
        Optional<String> name = message.sender().name();
        return MessageCreateSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                        .author(message.sender().name().orElse("Anonymous"), null,
                                "https://mc-heads.net/avatar/%s".formatted(message.sender().id()))
                        .description(new MsgAdaptor(message)
                                .readString(ep -> new IElement[] {Elements.message()})
                        ).build()
                ).build().asRequest();
    }

    @Override
    public MultipartRequest<MessageCreateRequest> error(Exception e) {
        return MessageCreateSpec.create().withContent(e.toString()).asRequest();
    }

    @Override
    public MultipartRequest<MessageCreateRequest> acceptButton(List<Button> buttons) {
        return MessageCreateSpec.builder()
                .content("Received buttons:")
                .addComponent(ActionRow.of(
                    buttons.stream()
                            .map(b -> discord4j.core.object.component.Button.primary(
                                    b.trigger().trigger(),
                                    b.name()
                            )).toList()
                )).build().asRequest();
    }

    @Override
    public boolean checkPermission(String node) {
        return Optional.ofNullable(DiscordHook.configOfChannel(id()))
                .map(conf -> conf.checkPerm(node))
                .orElse(false);
    }
}
