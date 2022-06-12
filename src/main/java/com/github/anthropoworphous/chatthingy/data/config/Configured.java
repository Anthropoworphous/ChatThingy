package com.github.anthropoworphous.chatthingy.data.config;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.MapCache;
import org.bukkit.Bukkit;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class Configured {
    private static final List<Configured> configuredStuff = new ArrayList<>();
    protected static final File CONFIG_FOLDER = new File(ChatThingy.plugin().getDataFolder(), "config");

    private final MapCache<String, String, Object> cache = new MapCache<>(
            RandomCache::new, k -> new RandomCache<>()
    );
    private Ini config;

    protected Configured() {
        configuredStuff.add(this);
    }

    // get value
    protected String get(String section, String key) {
        return config.get(section, key);
    }

    @SuppressWarnings("unchecked")
    public <T> T parse(String section, String key, BiFunction<String, String, T> mapper) {
        return (T) cache.cacheNested(section, key, mapper::apply);
    }

    // other
    public Configured reload() {
        File file = new File(configFolder(), configFileName()+".ini");
        try {
            config = new Ini(file);
        } catch (IOException e) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            try {
                config = defaultIni();
                config.store(file);
                Bukkit.getLogger().info("Config file created at: %s".formatted(file.getAbsolutePath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }

    protected abstract String configFileName();
    protected abstract File configFolder();
    protected abstract Ini defaultIni();



    public static void reloadAll() {
        configuredStuff.forEach(Configured::reload);
    }
}