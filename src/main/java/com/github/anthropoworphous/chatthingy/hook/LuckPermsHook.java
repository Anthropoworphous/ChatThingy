package com.github.anthropoworphous.chatthingy.hook;

import com.github.anthropoworphous.chatthingy.task.impl.msg.wrapper.impl.LuckPermsWrapper;
import org.bukkit.Bukkit;

public class LuckPermsHook implements Hook {
    @Override
    public String hookName() {
        return "luckperms-hook";
    }

    public void init() {
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            new LuckPermsWrapper().register();
        }
    }
}
