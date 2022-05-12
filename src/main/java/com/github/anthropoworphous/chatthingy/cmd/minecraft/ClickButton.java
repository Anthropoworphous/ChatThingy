package com.github.anthropoworphous.chatthingy.cmd.minecraft;

import com.github.anthropoworphous.chatthingy.event.internal.EventBus;
import com.github.anthropoworphous.chatthingy.event.internal.Trigger;
import com.github.anthropoworphous.cmdlib.command.CMD;
import com.github.anthropoworphous.cmdlib.command.variable.addition.impl.Check;
import com.github.anthropoworphous.cmdlib.command.variable.impl.StringVar;

import java.util.List;

public class ClickButton implements CMD {
    @Override
    public List<Route> routes() {
        return Routes.single(
                (commandSender, list) -> {
                    commandSender.sendMessage(
                            EventBus.trigger(new Trigger((String) list.get(0))) ? "success" : "failed");
                    return true;
                },
                new StringVar().additional(new Check<>(EventBus::exist))
        );
    }

    @Override
    public String name() {
        return "click-button";
    }
}
