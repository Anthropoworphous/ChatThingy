package com.github.anthropoworphous.chatthingy.data.config.hook;

import com.github.anthropoworphous.chatthingy.data.config.ConfigCache;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.hook.Hook;
import org.ini4j.Ini;

import java.util.Optional;
import java.util.function.Function;

public class HookConfig {
    private static final ConfigCache CACHE = new ConfigCache(
            StringKey::str,
            k -> {
                Ini ini = new Ini();

                if (k.str().equals("discord")) {
                    ini.put("connection", "token",
                            "0000000000000000000000000000000000000000000000000000000000000000000000");
                }

                return ini;
            });

    public static Optional<ConfigCache.Config> load(Hook e) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.hookName())));
    }

    public static <T> T loadOr(Hook e, Function<ConfigCache.Config, T> getter, T defaultValue) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.hookName()))).map(getter).orElse(defaultValue);
    }
    public static void reload(Hook e) {
        CACHE.reload(new StringKey(e.hookName()));
    }
}
