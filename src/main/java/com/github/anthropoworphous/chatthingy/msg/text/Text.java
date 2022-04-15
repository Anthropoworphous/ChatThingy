package com.github.anthropoworphous.chatthingy.msg.text;

import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Text<W extends IWord<?>, R> {
    public Text(List<? extends IWord<?>> words) {
        this.words = new ArrayList<>(words);
    }

    // Have to be mutable
    protected final List<IWord<?>> words;

    protected abstract Optional<W> convert(IWord<?> word);

    public Optional<List<W>> contents() {
        List<W> result = new ArrayList<>();
        for (IWord<?> w : words) {
            Optional<W> casted = convert(w);
            if (casted.isEmpty()) { return Optional.empty(); }
            result.add(casted.get());
        }
        return Optional.of(result);
    }

    public void set(int Index, IWord<?> Word) {
        words.set(Index, Word);
    }

    public List<IWord<?>> getRawWords() { return words; }


    public abstract Optional<R> result();
}
