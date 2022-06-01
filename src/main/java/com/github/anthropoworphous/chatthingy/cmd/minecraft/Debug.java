package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.cmdlib.command.CMD;
import com.github.anthropoworphous.cmdlib.command.variable.addition.impl.AutoComplete;
import com.github.anthropoworphous.cmdlib.command.variable.impl.StringVar;
import discord4j.core.object.entity.ApplicationInfo;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class Debug implements CMD {
    private static final Route infoRoute = // a bit too big
            new Route(
                    (commandSender, list) -> {
                        switch ((String) list.get(0)) {
                            case "Discord" -> {
                                commandSender.sendMessage("Discord %s".formatted(
                                        DiscordHook.client().isEmpty() ? "not connected" : "connected")
                                );
                                DiscordHook.client().ifPresentOrElse(client -> client
                                                .getApplicationInfo()
                                                .blockOptional(Duration.ofSeconds(10))
                                                .map(ApplicationInfo::getData)
                                                .ifPresentOrElse(data -> commandSender.sendMessage(
                                                        "Name: %s".formatted(data.name()),
                                                        "Up Time: %s".formatted(DiscordHook.upTime()),
                                                        "About: %s".formatted(data.description()
                                                                .replaceAll(",?\\s", ", "))
                                                ), () -> commandSender.sendMessage("Failed to retrieve data"))
                                        , () -> commandSender.sendMessage("Discord not connected"));
                            }
                            case "LuckPerms" -> commandSender.sendMessage("idk lmao");
                            default -> commandSender.sendMessage("wut");
                        }
                        return true;
                    },
                    new StringVar().additional(new AutoComplete<>(() -> List.of(
                            "Discord",
                            "LuckPerms"
                    )))
            );

    @Override
    public List<Route> routes() {
        return Routes.of(
                infoRoute,
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
