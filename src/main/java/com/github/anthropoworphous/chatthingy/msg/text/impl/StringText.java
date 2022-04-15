package com.github.anthropoworphous.chatthingy.msg.text.impl;

import com.github.anthropoworphous.chatthingy.msg.text.Text;
import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.text.word.impl.PureString;

import java.util.Optional;
import java.util.stream.Stream;

public class StringText extends Text<PureString, String> {
    public StringText(String msg) {
        super(Stream.of(msg.split(" ")).map(PureString::new).toList());
    }

    @Override
    public Optional<PureString> convert(IWord<?> word) {
        return PureString.convert(word);
    }

    @Override
    public Optional<String> result() {
        StringBuilder b = new StringBuilder();
        return contents().map(list -> {
            list.forEach(w -> b.append(w.getContent()));
            return b.toString();
        });
    }
}
