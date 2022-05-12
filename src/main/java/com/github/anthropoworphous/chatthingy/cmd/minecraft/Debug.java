package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.cmdlib.command.CMD;
import com.github.anthropoworphous.cmdlib.command.variable.addition.impl.AutoComplete;
import com.github.anthropoworphous.cmdlib.command.variable.impl.StringVar;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class Debug implements CMD {
    @Override
    public List<Route> routes() {
        return Routes.of(
                new Route(
                        (commandSender, list) -> {
                            switch ((String) list.get(0)) {
                                case "Discord" -> commandSender.sendMessage("idk lmao");
                                case "LuckPerms" -> commandSender.sendMessage("idk as well lmao");
                                default -> commandSender.sendMessage("wut");
                            }
                            return true;
                        },
                        new StringVar().additional(new AutoComplete<>(() -> List.of(
                                "Discord",
                                "LuckPerms"
                        )))
                ),
                new Route(
                        (commandSender, list) -> {
                            commandSender.sendMessage(
                                    "Options:",
                                    " -  LuckPerms",
                                    " -  Discord"
                            );
                            return true;
                        }
                )
        );
    }

    @Override
    public @NotNull String name() {
        return "debug";
    }

    @Override
    public Optional<String> permission() {
        return Optional.of("chat_thingy.debug");
    }
}
