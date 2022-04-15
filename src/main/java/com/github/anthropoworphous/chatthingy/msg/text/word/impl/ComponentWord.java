package com.github.anthropoworphous.chatthingy.msg.text.word.impl;

import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Optional;

public class ComponentWord implements IWord<Component> {
    public ComponentWord(Component word) {
        this.word = word;
    }

    private final Component word;

    public static Optional<ComponentWord> convert(IWord<?> word) {
        TextComponent.Builder builder = Component.text();
        builder.append(Component.text(word.pure()));
        word.style().ifPresent(builder::style);

        return Optional.of(new ComponentWord(builder.build()));
    }

    @Override
    public Component getContent() {
        return word;
    }

    @Override
    public String pure() {
        return componentToString(word);
    }

    @Override
    public Optional<Style> style() {
        return Optional.of(word.style());
    }

    // helper method
    protected String componentToString(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
