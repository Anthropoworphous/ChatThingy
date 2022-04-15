package com.github.anthropoworphous.chatthingy.msg.text.impl;

import com.github.anthropoworphous.chatthingy.msg.text.Text;
import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.text.word.impl.ComponentWord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;

public class AdventureText extends Text<ComponentWord, Component> {
    /**
     * Do not save the type, might cause error
     * @param text value to initiate message
     */
    public AdventureText(@NotNull Component text) {
        super(new ArrayList<>());
        Spliterator<Component> s = text.spliterator(ComponentIteratorType.BREADTH_FIRST, Set.of());

        while(s.tryAdvance(word -> words.add(new ComponentWord(word))));
    }

    @Override
    public Optional<ComponentWord> convert(IWord<?> word) {
        return ComponentWord.convert(word);
    }

    @Override
    public Optional<Component> result() {
        TextComponent.Builder b = Component.text();
        return contents().map(list -> {
            list.forEach(w -> b.append(w.getContent()));
            return b.build();
        });
    }
}
