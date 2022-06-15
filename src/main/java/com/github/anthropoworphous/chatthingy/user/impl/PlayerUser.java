package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.adaptor.MessageAdaptor;
import com.github.anthropoworphous.chatthingy.msg.elements.Elements;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PlayerUser extends User<Component> {
    public final Player player;

    public PlayerUser(@NotNull Player player) {
        super(player.getUniqueId().toString());
        this.player = player;
    }

    @Override
    protected void accept(Component content) {
        player.sendMessage(content);
    }

    @Override
    protected Component read(IMessage message) {
        return new MessageAdaptor(message).readComponent(ep -> new IElement[] {
                ep.lookAhead(Elements.openBracket()),
                Elements.channelName(),
                ep.lookBehind(Elements.closeBracket()),
                ep.lookAhead(Elements.openBracket()),
                ep.decodeColorCode(Elements.senderPrefix()),
                ep.lookBehind(Elements.closeBracket()),
                Elements.senderName(),
                ep.lookBehind(Elements.messageIndicator()),
                ep.elementHover(Elements.message(), Elements.timestamp())
        });
    }

    @Override
    protected Component error(Exception e) {
        e.printStackTrace();
        return Component.text(e.toString()).color(NamedTextColor.RED);
    }

    @Override
    protected Component acceptButton(List<Button> buttons) {
        TextComponent.Builder cb = Component.text()
                .append(Component.text("Buttons received: "));
        for (Button button : buttons) {
            cb.append(Component.text("["))
                    .append(Component.text(button.name())
                            .color(button.color())
                            .hoverEvent(HoverEvent.hoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.text(button.trigger().trigger())))
                            .clickEvent(ClickEvent.clickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/click-button %s".formatted(button.trigger().trigger())
                            )))
                    .append(Component.text("] "));
        }
        return cb.build();
    }

    @Override
    public boolean checkPermission(String node) {
        return player.hasPermission(node);
    }

    @Override
    public Optional<String> name() {
        return Optional.of(player.getName());
    }
}