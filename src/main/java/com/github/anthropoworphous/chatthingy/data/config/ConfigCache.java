package com.github.anthropoworphous.chatthingy.data.config;

import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.key.StringKey;
import org.bukkit.Bukkit;
import org.ini4j.Ini;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ConfigCache {
    private final Function<StringKey, Ini> defaultIniConstructor;
    private final Function<StringKey, String> folderName;
    private final Map<StringKey, Config> cache = new HashMap<>();

    public ConfigCache(Function<StringKey, String> folderName, Function<StringKey, Ini> defaultIniConstructor) {
        this.defaultIniConstructor = defaultIniConstructor;
        this.folderName = folderName;
    }

    @Nullable
    public Config load(StringKey key) {
        if (!cache.containsKey(key)) {
            reload(key);
        }
        return cache.get(key);
    }

    public void reload(StringKey key) {
        cache.clear();
        File folder = ConfigManager.getDataFolder().resolve(folderName.apply(key)).toFile();

        if (folder.mkdirs()) {
            Bukkit.getLogger().info("config folder %1$s created at %2$s"
                    .formatted(key.str(), folder.getAbsolutePath()));
        }

        File channelFile = new File(folder, key.str() + ".ini");

        Ini ini;
        try {
            ini = new Ini(channelFile);
        } catch (Exception e) {
            ini = defaultIniConstructor.apply(key);
        }

        try {
            ini.store(channelFile);
            cache.put(key, new Config(ini));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Config {
        private final RandomCache<StringKey, Object> cache = new RandomCache<>();
        private final Ini config;

        public Config(Ini config) {
            this.config = config;
        }

        public String get(String section, String configKey) {
            return config.get(section, configKey);
        }

        @SuppressWarnings("unchecked")
        public <T> T parse(String section, String configKey, Function<String, T> mapper) {
            return (T) cache.cache(new StringKey(section + configKey), key -> mapper.apply(get(section, configKey)));
        }
    }
}
