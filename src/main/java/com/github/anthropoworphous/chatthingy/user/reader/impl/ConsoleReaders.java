package com.github.anthropoworphous.chatthingy.user.reader.impl;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.Result;
import com.github.anthropoworphous.chatthingy.msg.text.impl.StringText;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.reader.Readers;
import org.bukkit.Bukkit;

public class ConsoleReaders extends ConsoleUser implements Readers {
    @Override
    public Result read(Message msg) {
        Bukkit.getLogger().info(msg.content(StringText.class).result().orElse("Unable to convert message"));
        return new Result(Result.Status.FINE);//just log the msg and return fine
    }
}
