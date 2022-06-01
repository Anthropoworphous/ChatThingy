package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.adaptor.MsgAdaptor;
import com.github.anthropoworphous.chatthingy.msg.elements.Elements;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.user.User;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConsoleUser extends User<String> {
    private static final String id = UUID.randomUUID().toString();
    public ConsoleUser() {
        super(id);
    }

    @Override
    protected void accept(String content) {
        Bukkit.getLogger().info(content);
    }

    @Override
    public String read(Message msg) {
        return new MsgAdaptor(msg).readString(ep -> new IElement[]{
                ep.lookAhead(Elements.openBracket()),
                Elements.channelName(),
                ep.lookBehind(Elements.closeBracket()),
                Elements.senderName(),
                ep.lookBehind(Elements.messageIndicator()),
                Elements.message(),
                Elements.space(),
                Elements.timestamp()
        });
    }

    @Override
    public String error(Exception e) {
        return e.toString();
    }

    @Override
    public String acceptButton(List<Button> buttons) {
        StringBuilder sb = new StringBuilder();
        sb.append("Button received: ");
        for (Button button : buttons) {
            sb.append(button.name())
                    .append("[")
                    .append(button.trigger().trigger())
                    .append("] || ");
        }
        sb.delete(sb.length()-4, sb.length());
        return sb.toString();
    }

    @Override
    public boolean checkPermission(String node) {
        // this is console, have all permission
        return true;
    }

    @Override
    public Optional<String> name() {
        return Optional.of("console");
    }

    @Override
    public Optional<String> prefix() {
        return Optional.of("Console");
    }

    @Override
    public Optional<List<String>> bio() {
        return Optional.of(List.of("Logging ip since 1690"));
    }

    @Override
    public Optional<List<String>> descriptions() {
        return Optional.of(List.of("Just a console"));
    }
}
