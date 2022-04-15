package com.github.anthropoworphous.chatthingy.user.reader.impl;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.Result;
import com.github.anthropoworphous.chatthingy.msg.text.impl.AdventureText;
import com.github.anthropoworphous.chatthingy.user.reader.Readers;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class OnlinePlayersReaders implements Readers {
    @Override
    public Result read(Message msg) {
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(msg.content(AdventureText.class).result().orElseThrow()));
        return new Result(Result.Status.FINE);
    }

    @Override
    public @NotNull String name() {
        return "Online players";
    }
}
