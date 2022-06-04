package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.word.extend.PureString;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;

import java.util.List;

public class ExpendSlang implements Interceptor {
    @Override
    public void intercept(Message msg) {
        msg.getContent().opGet().ifPresent(list -> {
            for (int i = 0; i < list.size(); i++) {
                // lol, lmao is specifically excluded
                switch (list.get(i).text().toLowerCase()) {
                    case "afk" -> chainAppend(list, i, "away", "from", "keyboard");
                    case "asap" -> chainAppend(list, i, "as", "soon", "as", "possible");
                    case "btw" -> chainAppend(list, i, "by", "the", "way");
                    case "brb" -> chainAppend(list, i, "be", "right", "back");
                    case "fyi" -> chainAppend(list, i, "for", "your", "information");
                    case "ffs" -> chainAppend(list, i, "for", "fuck's", "sake");
                    case "gg" -> chainAppend(list, i, "good", "game");
                    case "iirc" -> chainAppend(list, i, "if", "I", "recall", "correctly");
                    case "imo" -> chainAppend(list, i, "in", "my", "opinion");
                    case "irl" -> chainAppend(list, i, "in", "real", "life");
                    case "istg" -> chainAppend(list, i, "I", "swear", "to", "god");
                    case "jk" -> chainAppend(list, i, "just", "kidding");
                    case "k" -> chainAppend(list, i, "ok");
                    case "kys" -> chainAppend(list, i, "kill", "yourself");
                    case "nvm" -> chainAppend(list, i, "never", "mind");
                    case "np" -> chainAppend(list, i, "no", "problem");
                    case "nsfw" -> chainAppend(list, i, "not", "safe", "for", "work");
                    case "pls" -> chainAppend(list, i, "please");
                    case "stfu" -> chainAppend(list, i, "shut", "the", "fuck", "up");
                    case "sfw" -> chainAppend(list, i, "safe", "for", "work");
                    case "thx" -> chainAppend(list, i, "thanks");
                    case "tbh" -> chainAppend(list, i, "to", "be", "honest");
                    case "u" -> chainAppend(list, i, "you");
                    case "ur" -> chainAppend(list, i, "you", "are");
                    case "wtf" -> chainAppend(list, i, "what", "the", "fuck");
                }
            }
        });
    }

    private void chainAppend(List<IWord> words, int index, String... toAdd) {
        words.remove(index++);
        for (String str : toAdd) {
            words.add(index++, new PureString(str));
        }
    }
}
