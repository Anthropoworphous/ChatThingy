package com.github.anthropoworphous.chatthingy.task.wrapper;

import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.extend.LuckPermUser;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class LuckPermsWrapper implements Wrapper {
    @Override
    public void wrap(IMessage msg) {
        Optional.ofNullable(Bukkit.getServicesManager().getRegistration(LuckPerms.class)).ifPresent(lp -> {
            LuckPerms api = lp.getProvider();
            if (msg.sender() instanceof PlayerUser) {
                Player player = ((PlayerUser) msg.sender()).player;
                User user = api.getPlayerAdapter(Player.class).getUser(player);
                msg.sender(new LuckPermUser(player, user));
            }
        });
    }
}
