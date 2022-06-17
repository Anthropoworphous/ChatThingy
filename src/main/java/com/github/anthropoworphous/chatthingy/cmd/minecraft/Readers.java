package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.group.AllReaders;
import com.github.anthropoworphous.cmdlib.command.CMD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Readers implements CMD {
    @Override
    public List<Route> routes() {
        return Routes.singleNoArg((commandSender, list) -> {
            Map<String, List<String>> readers = new HashMap<>();
            for (User<?> user : new AllReaders()) {
                readers.computeIfAbsent(
                        user.getClass()
                                .getSimpleName()
                                .replaceAll("([A-Z])", " $1")
                                .toLowerCase(),
                        k -> new ArrayList<>()
                ).add(user.name().orElse(user.id()));
            }
            readers.forEach((type, users) -> {
                commandSender.sendMessage("--%s--".formatted(type));
                commandSender.sendMessage(String.join(" ", users));
            });
            return true;
        });
    }

    @Override
    public String name() { return "all-readers"; }
}
