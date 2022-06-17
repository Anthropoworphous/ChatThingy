package com.github.anthropoworphous.chatthingy.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ColorCodeDecoder {
    public static String strip(String input) {
        return input.replaceAll("&([\\da-fk-lr])|<#([\\da-f]{6})>", "");
    }

    public static Component decode(String input) {
        Matcher matcher = Pattern.compile("&(r)|&([\\da-f])|&([k-l])|<#([\\da-f]{6})>").matcher(input);
        TextComponent.Builder b = Component.text();
        Style style = Style.empty();
        int lastCut = 0;

        while (matcher.find()) {
            b.append(Component.text(input.substring(lastCut, matcher.start())).style(style));
            lastCut = matcher.end();
            Style.Builder newStyle = Style.style();

            if (matcher.group(1) != null) {
                newStyle = Style.style();
                style = Style.empty();
            } else if (matcher.group(2) != null) {
                newStyle.color(ColorCode.get(matcher.group(2).charAt(0)));
            } else if (matcher.group(3) != null) {
                newStyle.decoration(StyleCode.get(matcher.group(3).charAt(0)), true);
            } else if (matcher.group(4) != null) {
                try {
                    newStyle.color(TextColor.color(
                            Integer.parseInt(matcher.group(4).substring(0, 2), 16),
                            Integer.parseInt(matcher.group(4).substring(2, 4), 16),
                            Integer.parseInt(matcher.group(4).substring(4), 16)
                    ));
                } catch (Exception ignored) {}
            }

            style = newStyle.merge(style, Style.Merge.Strategy.IF_ABSENT_ON_TARGET).build();
        }

        return b.append(Component.text(input.substring(lastCut)).style(style)).build().compact();
    }

    public enum ColorCode {
        BLACK('0', TextColor.color(0, 0, 0)),
        BLUE('1', TextColor.color(0, 0, 170)),
        GREEN('2', TextColor.color(0, 170, 0)),
        CYAN('3', TextColor.color(0, 170, 170)),
        RED('4', TextColor.color(170, 0, 0)),
        PURPLE('5', TextColor.color(170, 0, 170)),
        ORANGE('6', TextColor.color(255, 170, 0)),
        LIGHT_GREY('7', TextColor.color(170, 170, 170)),
        GREY('8', TextColor.color(85, 85, 85)),
        LIGHT_BLUE('9', TextColor.color(85, 85, 255)),
        LIGHT_GREEN('a', TextColor.color(85, 255, 85)),
        LIGHT_CYAN('b', TextColor.color(85, 255, 255)),
        LIGHT_RED('c', TextColor.color(255, 85, 85)),
        MAGENTA('d', TextColor.color(255, 85, 255)),
        YELLOW('e', TextColor.color(255, 255, 85)),
        WHITE('f', TextColor.color(255, 255, 255));

        ColorCode(Character c, TextColor color) {
            this.c = c;
            this.color = color;
        }

        private final char c;
        private final TextColor color;

        public static TextColor get(Character c) {
            for (ColorCode ccc : ColorCode.values()) {
                if (ccc.c == c) { return ccc.color; }
            }
            return NamedTextColor.WHITE;
        }
    }
    public enum StyleCode {
        OBFUSCATED('k', TextDecoration.OBFUSCATED),
        BOLD('l', TextDecoration.BOLD),
        STRIKETHROUGH('m', TextDecoration.STRIKETHROUGH),
        DECORATION('n', TextDecoration.UNDERLINED),
        ITALIC('o', TextDecoration.ITALIC);

        StyleCode(Character c, TextDecoration decoration) {
            this.c = c;
            this.decoration = decoration;
        }

        private final char c;
        private final TextDecoration decoration;

        public static TextDecoration get(Character c) {
            for (StyleCode ccc : StyleCode.values()) {
                if (ccc.c == c) { return ccc.decoration; }
            }
            return TextDecoration.ITALIC; //impossible
        }
    }
}
