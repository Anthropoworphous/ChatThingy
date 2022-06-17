package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.chatthingy.data.config.Configuration;
import com.github.anthropoworphous.cmdlib.command.CMD;

import java.util.List;
import java.util.Optional;

public class ReloadConfig implements CMD {
    @Override
    public List<Route> routes() {
        return Routes.singleNoArg(
                (commandSender, list) -> {
                    Configuration.reloadAll();
                    return true;
                }
        );
    }

    @Override
    public String name() { return "reload-config"; }

    @Override
    public Optional<String> permission() { return Optional.of("chat_thingy.reload_config"); }
}
