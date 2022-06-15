package com.github.anthropoworphous.chatthingy.channel;

import com.github.anthropoworphous.chatthingy.data.config.BukkitConfiguration;
import com.github.anthropoworphous.chatthingy.data.config.Configuration;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import org.ini4j.Ini;

import java.io.File;
import java.util.Optional;

public abstract class Channel {
    private final Configuration config = generateConfig();
    public String channelName() {
        return getClass().getSimpleName();
    }

    public void apply(IMessage msg) throws Exception {
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

    public Configuration generateConfig() {
        return new BukkitConfiguration.Builder()
                .name(getClass().getSimpleName())
                .folder(f -> new File(f, "channels"))
                .defaultIniCreator(this::defaultIni)
                .build();
    }

    public String readPerm() {
        return config.get("permission", "readPerm");
    }
    public String sendPerm() {
        return config.get("permission", "sendPerm");
    }

    protected abstract Ini defaultIni();

    public abstract Optional<String> name();
    public abstract Optional<String> prefixTrigger();
}
