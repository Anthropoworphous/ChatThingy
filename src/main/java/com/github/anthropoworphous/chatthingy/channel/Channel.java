package com.github.anthropoworphous.chatthingy.channel;

import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.msg.Message;

import java.io.File;
import java.util.Optional;

public abstract class Channel extends Configured {
    public String channelName() {
        return getClass().getSimpleName();
    }

    public void apply(Message msg) throws Exception {
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

    public String readPerm() {
        return get("permission", "readPerm");
    }
    public String sendPerm() {
        return get("permission", "sendPerm");
    }

    @Override
    protected String configFileName() {
        return getClass().getSimpleName();
    }

    @Override
    protected File configFolder() {
        return new File(CONFIG_FOLDER, "channels");
    }

    public abstract Optional<String> name();

    public abstract Optional<String> prefixTrigger();
}
