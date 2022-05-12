package com.github.anthropoworphous.chatthingy.data.config.event;

import com.github.anthropoworphous.chatthingy.data.config.ConfigCache;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.event.Event;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.ini4j.Ini;

import java.util.Optional;
import java.util.function.Function;

public class EventConfig {
    private static final ConfigCache CACHE = new ConfigCache(
            k -> "events/%s".formatted(k.str()),
            k -> {
                Ini ini = new Ini();
                ini.putComment("text",
                        "<p> will be replaced by name of player");
                ini.putComment("color",
                        "color is rbg value separated by \",\" range from 0 ~ 255");
                ini.put("text", "join-message", "[+] <p>");
                ini.put("text", "leave-message", "[-] <p>");
                TextColor c1 = NamedTextColor.GREEN;
                TextColor c2 = NamedTextColor.RED;
                ini.put("color", "join-color",
                        "%1$d,%2$d,%3$d".formatted(c1.red(), c1.green(), c1.blue()));
                ini.put("color", "leave-color",
                        "%1$d,%2$d,%3$d".formatted(c2.red(), c2.green(), c2.blue()));
                return ini;
            });

    public static Optional<ConfigCache.Config> load(Event e) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.eventName())));
    }

    public static <T> T loadOr(Event e, Function<ConfigCache.Config, T> getter, T defaultValue) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.eventName()))).map(getter).orElse(defaultValue);
    }

    public static void reload(Event e) {
        CACHE.reload(new StringKey(e.eventName()));
    }
}
