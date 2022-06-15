package com.github.anthropoworphous.chatthingy.msg.interceptor.minecraft;

import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;

import java.util.Optional;

public class HighLightMCName implements Interceptor {
    @Override
    public void intercept(IMessage msg) throws Exception {
        for (IWord w : msg.getContent().get()) {
            Optional.ofNullable(Bukkit.getPlayerExact(w.text()))
                    .ifPresent(p -> {
                        w.text('@' + w.text());
                        w.style(Style.style()
                                .color(NamedTextColor.YELLOW)
                                .build());
                    });
        }
    }
}
