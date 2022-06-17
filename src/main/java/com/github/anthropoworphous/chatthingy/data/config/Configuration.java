package com.github.anthropoworphous.chatthingy.data.config;

import com.github.anthropoworphous.chatthingy.error.handling.throwable.ThrowableBiFunction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public interface Configuration {
    Map<String, Configuration> configurations = new HashMap<>();

    // getter
    String get(String section, String key);
    default <T> T parse(String section, String key, BiFunction<String, String, T> mapper) {
        return mapper.apply(section, key);
    }
    default <T> T parseOrIfFailed(
            String section,
            String key,
            ThrowableBiFunction<String, String, T> mapper,
            T backupValue
    ) {
        try {
            return mapper.map(section, key).orElse(backupValue);
        } catch (Exception e) { return backupValue; }
    }

    // file io
    File folder();
    String name();
    void reload();

    // static
    static void reloadAll() {
        configurations.values().forEach(Configuration::reload);
    }

    abstract class Configured {
        private static Configuration config = null;

        public Configured() {
            if (config == null) { config = generateConfig(); }
        }

        public static Configuration config() { return config; }

        protected abstract Configuration generateConfig();
    }
}
