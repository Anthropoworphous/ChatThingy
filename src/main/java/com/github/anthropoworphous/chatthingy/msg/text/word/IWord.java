package com.github.anthropoworphous.chatthingy.msg.text.word;

import net.kyori.adventure.text.format.Style;

import java.util.Optional;

public interface IWord<T> {
    String pure();
    Optional<Style> style();

    T getContent();
}
