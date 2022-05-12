package com.github.anthropoworphous.chatthingy.msg.word.extend;

import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.word.impl.ComponentWord;
import net.kyori.adventure.text.Component;

public class PureString extends ComponentWord implements IWord {
    public PureString(String word) {
        super(Component.text(word));
    }
}
