package com.github.anthropoworphous.chatthingy.data.config.interceptor;

import com.github.anthropoworphous.chatthingy.data.config.ConfigCache;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter.Censorship;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter.SpamFilter;
import org.ini4j.Ini;

import java.util.Optional;
import java.util.function.Function;

public class InterceptorConfig {
    private static final ConfigCache CACHE = new ConfigCache(
            StringKey::str,
            k -> {
                Ini ini = new Ini();
                String name = SpamFilter.get().interceptorName();
                ini.putComment("threshold", "The higher it is the easier is it to trigger spam");
                ini.put(name, "threshold", "15");
                ini.put(name, "bypass", "Joe,Mama");
                name = Censorship.get().interceptorName();
                ini.putComment("blacklist", "words need to be surrounded by '\"'");
                // man it feels wrong to type what's in the blacklist
                ini.put(name, "blacklist", "\"nigger\",\"fuck\",\"bitch\"");
                return ini;
            });

    public static Optional<ConfigCache.Config> load(Interceptor e) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.interceptorName())));
    }

    public static <T> T loadOr(Interceptor e, Function<ConfigCache.Config, T> getter, T defaultValue) {
        return Optional.ofNullable(CACHE.load(new StringKey(e.interceptorName()))).map(getter).orElse(defaultValue);
    }

    public static void reload(Interceptor e) {
        CACHE.reload(new StringKey(e.interceptorName()));
    }
}
