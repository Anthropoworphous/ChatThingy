package com.github.anthropoworphous.chatthingy.data.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public interface Configuration {
    Map<String, Configuration> configurations = new HashMap<>();

    // get value
    File folder();
    String name();

    String get(String section, String key);
    default <T> T parse(String section, String key, BiFunction<String, String, T> mapper) {
        return mapper.apply(section, key);
    }

    // file io
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

        protected Configuration config() {
            return config;
        }

        protected abstract Configuration generateConfig();
    }
}
