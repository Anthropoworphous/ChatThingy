package com.github.anthropoworphous.chatthingy.msg.text.word.impl;

import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;
import net.kyori.adventure.text.format.Style;

import java.util.Optional;

public class PureString implements IWord<String> {
    public PureString(String word) {
        this.word = word;
    }

    public static Optional<PureString> convert(IWord<?> word) {
        return Optional.of(new PureString(word.pure()));
    }

    private final String word;

    @Override
    public String getContent() {
        return word;
    }

    @Override
    public String pure() {
        return word;
    }
    @Override
    public Optional<Style> style() {
        return Optional.empty();
    }
}
