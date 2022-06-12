package com.github.anthropoworphous.chatthingy.user.group;

import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class OnlinePlayerReaders extends ArrayList<PlayerUser> {
    public OnlinePlayerReaders() {
        super(Bukkit.getOnlinePlayers().stream().map(PlayerUser::new).toList());
    }

    @Override
    public User<?>[] toArray() {
        return this.toArray(PlayerUser[]::new);
    }
}
