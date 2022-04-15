package com.github.anthropoworphous.chatthingy.msg.text.word.extend;

import com.github.anthropoworphous.chatthingy.msg.text.word.impl.ComponentWord;
import com.github.anthropoworphous.chatthingy.util.ColorCodeDecoder;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Optional;

public class MinecraftColorCodedWord extends ComponentWord {
    // Color code like &1, &2 or <#123456>
    public MinecraftColorCodedWord(String word) {
        super(ColorCodeDecoder.parse(word));
    }

    // idk if this is a good idea, let me know @future-me
    @Override
    public String pure() {
        return styleToString(getContent().style(), super.pure());
    }

    private String styleToString(Style s, String str) {
        if (s.isEmpty()) { return ""; }
        StringBuilder sb = new StringBuilder();
        // add color
        Optional.ofNullable(s.color()).map(
                c -> sb.append("<#").append(c.asHexString()).append('>')
        );
        // add text format
        s.decorations().forEach((dec, state) -> {
            if (state == TextDecoration.State.TRUE) {
                switch (dec) {
                    case OBFUSCATED -> sb.append("&k");
                    case BOLD -> sb.append("&l");
                    case STRIKETHROUGH -> sb.append("&m");
                    case UNDERLINED -> sb.append("&n");
                    case ITALIC -> sb.append("&o");
                }
            }
        });

        sb.append(str);

        //reset
        if (!sb.isEmpty()) {
            sb.append("&r");
        }

        return sb.toString();
    }
}
