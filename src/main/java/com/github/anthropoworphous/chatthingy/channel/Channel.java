package com.github.anthropoworphous.chatthingy.channel;

import com.github.anthropoworphous.chatthingy.data.config.channel.ChannelConfig;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.msg.Message;

import java.util.Optional;

public interface Channel {
    default StringKey getKey() { return new StringKey(channelName()); }
    default String channelName() {
        return getClass().getSimpleName();
    }

    default void apply(Message msg) throws Exception {
        msg.channel(this);

        if (!sendPerm().equals("null") && !msg.sender().checkPermission(sendPerm())) {
            throw new Exception("Missing permission to send!");
        }

        if (!readPerm().equals("null")) {
            msg.readers().removeIf(reader -> reader.checkPermission(readPerm()));
        }

        if (msg.readers().size() == 0) {
            throw new Exception("No reader have the permission to read this message!");
        }
    }

    default String readPerm() {
        return ChannelConfig.loadOr(
                this,
                c -> c.get("permission", "readPerm"),
                defaultReadPerm());
    }
    default String sendPerm() {
        return ChannelConfig.loadOr(
                this,
                c -> c.get("permission", "sendPerm"),
                defaultSendPerm());
    }

    Optional<String> name();

    String defaultReadPerm();
    String defaultSendPerm();

    Optional<Character> trigger();
}
