package com.github.anthropoworphous.chatthingy.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.data.config.BukkitConfiguration;
import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.ini4j.Ini;

import java.io.File;
import java.util.List;

public class Censorship implements Interceptor {
    private static final BukkitConfiguration config = new BukkitConfiguration.Builder()
            .name("censor-ship")
            .folder(f -> new File(f, "interceptor"))
            .defaultIniCreator(() -> {
                Ini ini = new Ini();
                ini.putComment("censor", "no space, tech ain't that advance yet (I'm bad)");
                // man it feels wrong to type what's in the blacklist
                ini.put("censor", "words", "nigger,fuck,bitch");
                return ini;
            }).build();

    @Override
    public void intercept(IMessage msg) throws Exception {
        List<String> list = List.of(config.get("censor", "words").split(","));

        Style redStyle = Style.style()
                .color(NamedTextColor.RED)
                .build();

        for (IWord w : msg.getContent().get()) {
            if (list.contains(w.text())) {
                w.text("#".repeat(w.text().length()));
                w.style(redStyle);
            }
        }
    }
}
