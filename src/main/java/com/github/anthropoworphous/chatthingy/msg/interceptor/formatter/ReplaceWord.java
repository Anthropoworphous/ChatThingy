package com.github.anthropoworphous.chatthingy.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;

import java.util.Map;
import java.util.Optional;

public class ReplaceWord implements Interceptor {
    private final Map<String, String> table;

    public ReplaceWord(Map<String, String> table) {
        this.table = table;
    }

    @Override
    public void intercept(IMessage msg) throws Exception {
        for (IWord w : msg.getContent().get()) {
            Optional.ofNullable(table.get(w.text()))
                    .ifPresent(w::text);
        }
    }
}
