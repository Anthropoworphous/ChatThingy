package com.github.anthropoworphous.chatthingy.channel.impl;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import org.ini4j.Ini;

import java.util.Optional;

public class General extends Channel {
    @Override
    public Optional<String> name() {
        return Optional.empty();
    }

    @Override
    public Optional<String> prefixTrigger() {
        return Optional.empty();
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();
        ini.putComment("permission", "null means no permission is required");
        ini.put("permission", "readPerm", "null");
        ini.put("permission", "sendPerm", "null");
        return ini;
    }
}
