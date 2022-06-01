package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;

import java.util.Map;
import java.util.Optional;

public class ReplaceWord implements Interceptor {
    private final Map<String, String> table;

    public ReplaceWord(Map<String, String> table) {
        this.table = table;
    }

    @Override
    public void intercept(Message msg) throws Exception {
        for (IWord w : msg.getContent().get()) {
            Optional.ofNullable(table.get(w.text()))
                    .ifPresent(w::text);
        }
    }
}
