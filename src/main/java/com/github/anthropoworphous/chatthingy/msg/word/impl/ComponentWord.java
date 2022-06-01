package com.github.anthropoworphous.chatthingy.msg.word.impl;

import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.util.ComponentStringConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Nullable;

public class ComponentWord implements IWord {
    public ComponentWord(Component word) {
        this.word = word;
    }

    private @Nullable String pureCached = null;
    private Component word;

    @Override
    public String text() {
        return componentToString(word);
    }

    @Override
    public void text(String str) {
        word = Component.text().append(Component.text(str)).style(word.style()).build();
        pureCached = null;
    }

    @Override
    public Style style() {
        return word.style();
    }

    @Override
    public void style(Style style) {
        word = Component.text().append(Component.text(text())).style(style).build();
    }

    // helper method
    protected String componentToString(Component component) {
        if (pureCached == null) {
            pureCached = ComponentStringConverter.convert(component);
        }
        return pureCached;
    }
}
