package com.github.anthropoworphous.chatthingy.channel.impl.minecraft;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import org.ini4j.Ini;

import java.util.Optional;

public class Staff extends Channel {
    @Override
    public Optional<String> name() {
        return Optional.of("Staff");
    }

    @Override
    public Optional<String> prefixTrigger() {
        return Optional.of("!");
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();
        ini.putComment("permission", "null means no permission is required");
        ini.put("permission", "readPerm", "chat_thingy.staff_read");
        ini.put("permission", "sendPerm", "chat_thingy.staff_send");
        return ini;
    }
}
