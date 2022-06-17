package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.log.Logger;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.adaptor.MessageAdaptor;
import com.github.anthropoworphous.chatthingy.msg.elements.Elements;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConsoleUser extends User<String> {
    private static final Logger logger = new Logger();

    private static final String id = UUID.randomUUID().toString();
    public ConsoleUser() {
        super(id);
    }

    @Override
    protected void accept(String content) {
        if (content == null) { return; }
        logger.log(content);
    }

    @Override
    public String read(IMessage message) {
        return new MessageAdaptor(message).readString(ep -> new IElement[]{
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
        e.printStackTrace();
        return null;
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

    @Override public String pfpUrl() {
        return "https://mc-heads.net/avatar/console";
    }
    @Override public Optional<String> name() {
        return Optional.of("console");
    }
    @Override public Optional<String> prefix() {
        return Optional.of("Console");
    }
    @Override public Optional<List<String>> bio() {
        return Optional.of(List.of("Logging ip since 1690"));
    }
    @Override public Optional<List<String>> descriptions() {
        return Optional.of(List.of("Just a console"));
    }
}
