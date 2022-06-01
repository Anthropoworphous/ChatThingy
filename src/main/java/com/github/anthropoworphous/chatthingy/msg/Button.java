package com.github.anthropoworphous.chatthingy.msg;

import com.github.anthropoworphous.chatthingy.event.internal.EventBus;
import com.github.anthropoworphous.chatthingy.event.internal.Trigger;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public record Button(String name, Trigger trigger, TextColor color) {
    public Button(String name, Trigger trigger) {
        this(name, trigger, NamedTextColor.WHITE);
    }
    public void click() {
        EventBus.trigger(trigger);
    }
}
