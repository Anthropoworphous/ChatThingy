package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.ini4j.Ini;

import java.io.File;
import java.util.List;

public class Censorship extends Configured implements Interceptor {
    private static final Censorship instance = new Censorship();

    private Censorship() {}

    public static Censorship get() { return instance; }

    @Override
    public void intercept(Message msg) throws Exception {
        List<String> list = List.of(get("censor", "words").split(","));

        for (IWord w : msg.getContent().get()) {
            if (list.contains(w.text())) {
                w.text("#".repeat(w.text().length()));
                w.style(Style.style()
                        .color(NamedTextColor.RED)
                        .build());
            }
        }
    }

    @Override
    protected String configFileName() {
        return "censor-ship";
    }

    @Override
    protected File configFolder() {
        return new File(CONFIG_FOLDER, "interceptor");
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();
        ini.putComment("censor", "no space, tech ain't that advance yet (I'm bad)");
        // man it feels wrong to type what's in the blacklist
        ini.put("censor", "words", "nigger,fuck,bitch");
        return ini;
    }
}
