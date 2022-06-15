package com.github.anthropoworphous.chatthingy.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;

public class AutoCaps implements Interceptor {
    @Override
    public void intercept(IMessage msg) {
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
