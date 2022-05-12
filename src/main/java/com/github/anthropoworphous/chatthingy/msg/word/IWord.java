package com.github.anthropoworphous.chatthingy.msg.word;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

public interface IWord {
    String text();
    void text(String str);
    Style style();
    void style(Style style);
    default Component component() {
        return Component.text().append(Component.text(text()).style(style())).build();
    }
}
