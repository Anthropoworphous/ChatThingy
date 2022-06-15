package com.github.anthropoworphous.chatthingy.data.config;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.MapCache;
import org.bukkit.Bukkit;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BukkitConfiguration implements Configuration {
    private final MapCache<String, String, Object> cache = new MapCache<>(
            RandomCache::new, k -> new RandomCache<>()
    );
    private final File folder;
    private final String name;
    private final Supplier<Ini> defaultIniCreator;


    private Ini config;

    /**
     * Make sure it's only created once
     */
    private BukkitConfiguration(File folder, String name, Supplier<Ini> defaultIniCreator) {
        this.folder = folder;
        this.name = name;
        this.defaultIniCreator = defaultIniCreator;
        reload();
        configurations.put(folder.getAbsolutePath() + File.separator + name, this);
    }

    // get value
    @Override
    public File folder() {
        return folder;
    }
    @Override
    public String name() {
        return name;
    }

    @Override
    public String get(String section, String key) {
        return config.get(section, key);
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(String section, String key, BiFunction<String, String, T> mapper) {
        return (T) cache.cacheNested(section, key, mapper::apply);
    }

    // other
    @Override
    public void reload() {
        File file = new File(folder, name + ".ini");
        try {
            config = new Ini(file);
        } catch (IOException e) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
            try {
                config = defaultIniCreator.get();
                config.store(file);
                Bukkit.getLogger().info("Config file created at: %s".formatted(file.getAbsolutePath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // builder
    public static class Builder {
        private File folder = new File(ChatThingy.plugin().getDataFolder(), "config");
        private String name = "undefined";
        private Supplier<Ini> defaultIniCreator = Ini::new;

        public Builder folder(Function<File, File> folder) {
            this.folder = folder.apply(this.folder); return this;
        }
        public Builder name(String name) {
            this.name = name; return this;
        }
        public Builder defaultIniCreator(Supplier<Ini> defaultIniCreator) {
            this.defaultIniCreator = defaultIniCreator; return this;
        }

        public BukkitConfiguration build() {
            return new BukkitConfiguration(folder, name, defaultIniCreator);
        }
    }
}