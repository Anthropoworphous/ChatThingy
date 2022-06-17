package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.chatthingy.msg.message.Message;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.group.OnlinePlayerReaders;
import com.github.anthropoworphous.chatthingy.user.impl.ConsoleUser;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import com.github.anthropoworphous.cmdlib.command.CMD;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Only clear player's chat
 */
public class ClearChat implements CMD {
    @Override
    public List<Route> routes() {
        return Routes.singleNoArg(
                (commandSender, list) -> {
                    for (int i = 0; i < 100; i++) {
                        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(""));
                    }

                    User<String> consoleUser = new ConsoleUser();

                    new Message.Builder()
                            .sendBy(new EmptyUser<>())
                            .content("Chat cleared by " + (commandSender instanceof Player
                                    ? new PlayerUser((Player) commandSender).name().orElse("???")
                                    : consoleUser.name().orElseThrow()))
                            .readBy(new ReaderCollector(new OnlinePlayerReaders()))
                            .build().task().run();
                    return true;
                }
        );
    }

    @Override
    public String name() { return "clear-chat"; }

    @Override
    public Optional<String> permission() { return Optional.of("chat_thingy.clear_chat"); }
}
