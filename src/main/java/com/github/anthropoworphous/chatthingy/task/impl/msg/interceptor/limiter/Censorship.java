package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.data.config.interceptor.InterceptorConfig;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Censorship implements Interceptor {
    private static final Censorship instance = new Censorship();

    private Censorship() {}

    public static Censorship get() { return instance; }

    @Override
    public void intercept(Message msg) throws Exception {
        List<String> list = InterceptorConfig.loadOr(
                this,
                c -> c.parse(
                        interceptorName(),
                        "blacklist",
                        str -> Pattern.compile("(?:\"(\\w+)\",?)*")
                                .matcher(str)
                                .results()
                                .map(MatchResult::group)
                                .toList()
                ), Collections.emptyList());

        for (IWord w : msg.getContent().get()) {
            if (list.contains(w.text())) {
                w.text("#".repeat(w.text().length()));
                w.style(Style.style()
                        .color(NamedTextColor.RED)
                        .build());
            }
        }
    }
}
