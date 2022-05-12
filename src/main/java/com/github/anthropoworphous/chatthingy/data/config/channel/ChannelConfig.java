package com.github.anthropoworphous.chatthingy.data.config.channel;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.General;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Private;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Staff;
import com.github.anthropoworphous.chatthingy.data.config.ConfigCache;
import org.ini4j.Ini;

import java.util.Optional;
import java.util.function.Function;

public class ChannelConfig {
    private static final ConfigCache CACHE = new ConfigCache(
            k -> "channels/%s".formatted(k.str()),
            k -> {
                String readPerm = "undefined", sendPerm = "undefined";
                if (General.channel().channelName().equals(k.str())) {
                    readPerm = General.channel().defaultReadPerm();
                    sendPerm = General.channel().defaultSendPerm();
                } else if (Staff.channel().channelName().equals(k.str())) {
                    readPerm = Staff.channel().defaultReadPerm();
                    sendPerm = Staff.channel().defaultSendPerm();
                } else if (Private.channel().channelName().equals(k.str())) {
                    readPerm = Private.channel().defaultReadPerm();
                    sendPerm = Private.channel().defaultSendPerm();
                }
                Ini ini = new Ini();
                ini.putComment("permission", "null means no permission is required");
                ini.put("permission", "readPerm", readPerm);
                ini.put("permission", "sendPerm", sendPerm);

                return ini;
            });

    public static Optional<ConfigCache.Config> load(Channel e) {
        return Optional.ofNullable(CACHE.load(e.getKey()));
    }

    public static <T> T loadOr(Channel e, Function<ConfigCache.Config, T> getter, T defaultValue) {
        return Optional.ofNullable(CACHE.load(e.getKey())).map(getter).orElse(defaultValue);
    }
    public static void reload(Channel c) {
        CACHE.reload(c.getKey());
    }
}
