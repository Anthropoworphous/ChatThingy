package com.github.anthropoworphous.chatthingy;

import com.github.anthropoworphous.chatthingy.cmd.minecraft.ClearChat;
import com.github.anthropoworphous.chatthingy.cmd.minecraft.ClickButton;
import com.github.anthropoworphous.chatthingy.cmd.minecraft.Debug;
import com.github.anthropoworphous.chatthingy.cmd.minecraft.ReloadConfig;
import com.github.anthropoworphous.chatthingy.data.cache.complex.PersistentCache;
import com.github.anthropoworphous.chatthingy.data.config.Configuration;
import com.github.anthropoworphous.chatthingy.event.external.minecraft.ConsoleSayCommand;
import com.github.anthropoworphous.chatthingy.event.external.minecraft.InGameChat;
import com.github.anthropoworphous.chatthingy.event.external.minecraft.PlayerJoinLeave;
import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.hook.LuckPermsHook;
import com.github.anthropoworphous.cmdlib.processor.CMDRegister;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The amount of stuff I can strip off and use interdependently is incredible,
 * reinventing the wheel at its finest
 */
public class ChatThingy extends JavaPlugin {
    private static ChatThingy plugin = null;

    public static ChatThingy plugin() { return plugin; }

    @Override
    public void onEnable() {
        plugin = this;
        Configuration.reloadAll();

        // the stuff that might or might not load that aren't required
        new DiscordHook().init();
        new LuckPermsHook().init();

        Bukkit.getPluginManager().registerEvents(InGameChat.get(), this);
        Bukkit.getPluginManager().registerEvents(new ConsoleSayCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeave(), this);

        CMDRegister.registerCMD(new ClearChat(), this);
        CMDRegister.registerCMD(new ClickButton(), this);
        CMDRegister.registerCMD(new Debug(), this);
        CMDRegister.registerCMD(new ReloadConfig(), this);
    }

    @Override
    public void onDisable() {
        PersistentCache.saveAll();
    }

    public enum Permissions {
        IGNORE_SPAM_FILTER("spam_ignore");

        private static final String NODE_START = "chat_thingy";
        public final String node;

        Permissions(String permissionNode) {
            node = "%s.%s".formatted(NODE_START, permissionNode);
        }
    }
}
