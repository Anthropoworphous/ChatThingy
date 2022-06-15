package com.github.anthropoworphous.chatthingy.data.cache.complex;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.data.cache.Cache;
import com.github.anthropoworphous.chatthingy.data.cache.CacheFacade;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PersistentCache<K extends Serializable, V extends Serializable> implements CacheFacade<K, V> {
    // TODO handle unsafe deserialization issue
    private static final Set<PersistentCache<?, ?>> allPermCache = new HashSet<>();
    private static final Path SAVE_LOCATION = getDataFolder();

    private final Cache<K, V> cache;
    private final String name;

    public PersistentCache(String name, Supplier<Cache<K, V>> cache) {
        this.cache = cache.get();
        this.name = name;
        load();
        allPermCache.add(this);
    }
    public PersistentCache(int limit, String name, Supplier<Cache<K, V>> cache) {
        this.cache = cache.get();
        this.cache.limit(limit);
        this.name = name;
        load();
        allPermCache.add(this);
    }

    public static Path getDataFolder() {
        return ChatThingy.plugin().getDataFolder().toPath().resolve("data");
    }

    @Override
    public Cache<K, V> cache() {
        return cache;
    }

    private void save() {
        if (SAVE_LOCATION.toFile().mkdirs()) {
            Bukkit.getLogger().info("Cache file %1$s created at %2$s"
                    .formatted(name, SAVE_LOCATION.toFile().getAbsolutePath()));
        }
        File file = SAVE_LOCATION.resolve("%s.cache".formatted(name)).toFile();
        try (
                var objectOut = new ObjectOutputStream(new FileOutputStream(file))
        ) {
            objectOut.writeObject(new HashMap<>(cache.cache()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        var file = SAVE_LOCATION.resolve("%s.cache".formatted(name)).toFile();
        if (!file.exists()) {
            Bukkit.getLogger().info("Cache file not found");
            cache.clear();
            return;
        }
        try (
                var objectIn = new ObjectInputStream(new FileInputStream(file))
        ) {
            cache.putAll((Map<? extends K, ? extends V>) objectIn.readObject());
            cache.forEach((k, v) -> Bukkit.getLogger().info(k.toString() + "||" + v.toString()));
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
            cache.clear();
        }
    }

    public static void saveAll() {
        allPermCache.forEach(PersistentCache::save);
    }
}
