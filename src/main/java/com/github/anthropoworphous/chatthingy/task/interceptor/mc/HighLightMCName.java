package com.github.anthropoworphous.chatthingy.task.interceptor.mc;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.text.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.text.word.impl.ComponentWord;
import com.github.anthropoworphous.chatthingy.msg.text.word.impl.PureString;
import com.github.anthropoworphous.chatthingy.task.interceptor.Interceptor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.util.List;

public class HighLightMCName implements Interceptor {
    @Override
    public Message intercept(Message msg) {
        List<IWord<?>> words = msg.getRawContent().getRawWords();

        for (int i = 0; i < words.size(); i++) {
            int ii = i;
            PureString.convert(words.get(i))
                    .map(pure -> Bukkit.getPlayerExact(pure.getContent()))
                    .ifPresent(
                            player -> words.set(ii, new ComponentWord(
                                    Component.text('@' + player.getName())
                                            .style(
                                                    Style.style()
                                                            .color(TextColor.color(255, 170, 0))
                                                            .build()
                                            )

                                    //"&e%s&r".formatted(player.getName())
                            ))
                    );
        }

        return msg;
    }
}
