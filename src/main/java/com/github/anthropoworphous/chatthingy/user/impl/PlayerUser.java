package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerUser implements User {
    public PlayerUser(@NotNull Player player) {
        this.player = player;
    }

    protected final Player player;

    @Override
    public @NotNull String name() {
        return player.getName();
    }
}