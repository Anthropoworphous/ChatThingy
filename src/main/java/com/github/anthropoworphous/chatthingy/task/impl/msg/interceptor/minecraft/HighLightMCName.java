package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.minecraft;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;

import java.util.Optional;

public class HighLightMCName implements Interceptor {
    @Override
    public void intercept(Message msg) throws Exception {
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
