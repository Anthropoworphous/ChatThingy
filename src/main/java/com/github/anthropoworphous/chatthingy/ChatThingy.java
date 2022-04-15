package com.github.anthropoworphous.chatthingy;

import com.github.anthropoworphous.chatthingy.event.ConsoleSayCommand;
import com.github.anthropoworphous.chatthingy.event.MinecraftInGameChat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatThingy extends JavaPlugin {
    private static Plugin plugin;

    public static Plugin plugin() { return plugin; }

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getPluginManager().registerEvents(new MinecraftInGameChat(), this);
        Bukkit.getPluginManager().registerEvents(new ConsoleSayCommand(), this);
    }
}
