package com.github.anthropoworphous.chatthingy.user.reader.impl;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.Result;
import com.github.anthropoworphous.chatthingy.msg.text.impl.AdventureText;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import com.github.anthropoworphous.chatthingy.user.reader.Readers;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerReaders extends PlayerUser implements Readers {
    public PlayerReaders(@NotNull Player player) {
        super(player);
    }

    @Override
    public Result read(Message msg) {
        player.sendMessage(msg.content(AdventureText.class).result().orElseThrow());
        return new Result(Result.Status.FINE);
    }
}
