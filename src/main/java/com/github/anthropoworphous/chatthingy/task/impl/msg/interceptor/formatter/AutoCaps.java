package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;

public class AutoCaps implements Interceptor {
    private static final AutoCaps instance = new AutoCaps();

    private AutoCaps() {}

    public static AutoCaps get() { return instance; }

    @Override
    public void intercept(Message msg) {
        msg.getContent().opGet().ifPresent(list -> {
            for (int i = 0; i < list.size(); i++) {
                IWord word = list.get(i);
                if (i == 0) {
                    String firstChar = word.text().substring(0, 1);
                    if (firstChar.matches("[a-z]")) {
                        word.text(firstChar.toUpperCase() + word.text().substring(1));
                    }
                } else if (word.text().matches("\\bi(?:'\\w+)*\\b")) {
                    word.text("I");
                }
            }
        });
    }
}
