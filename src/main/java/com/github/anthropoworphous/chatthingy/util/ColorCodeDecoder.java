package com.github.anthropoworphous.chatthingy.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCodeDecoder {
    public static Component parse(String input) {
        return parse(
                Style.empty(),
                // reset | color | style | hex
                Pattern.compile("&(r)|&([0-9a-f])|&([k-l])|<#([0-9a-f]{6})>").matcher(input),
                input
        );
    }

    private static Component parse(Style style, Matcher matcher, String in) {
        TextComponent.Builder b = Component.text();

        // no match, return
        if (!matcher.find()) {
            return b.append(Component.text(in).style(style)).build();
        }

        if (matcher.group(1) != null) {
            style = Style.empty();
        } else if (matcher.group(2) != null) {
            style.edit(ColorCode.get(matcher.group(2).charAt(0)));
        } else if (matcher.group(3) != null) {
            style.edit(StyleCode.get(matcher.group(3).charAt(0)));
        } else if (matcher.group(4) != null) {
            style.color(TextColor.color(
                    Integer.parseInt(matcher.group(4).substring(0, 2), 16),
                    Integer.parseInt(matcher.group(4).substring(2, 4), 16),
                    Integer.parseInt(matcher.group(4).substring(4), 16)
            ));
        }

        return b.append(parse(
                style,
                matcher,
                in.substring(matcher.start() + (matcher.group(4) == null ? 2 : 9))
        )).build();
    }

    public enum ColorCode {
        BLACK('0', b -> b.color(TextColor.color(0, 0, 0))),
        BLUE('1', b -> b.color(TextColor.color(0, 0, 170))),
        GREEN('2', b -> b.color(TextColor.color(0, 170, 0))),
        CYAN('3', b -> b.color(TextColor.color(0, 170, 170))),
        RED('4', b -> b.color(TextColor.color(170, 0, 0))),
        PURPLE('5', b -> b.color(TextColor.color(170, 0, 170))),
        ORANGE('6', b -> b.color(TextColor.color(255, 170, 0))),
        LIGHT_GREY('7', b -> b.color(TextColor.color(170, 170, 170))),
        GREY('8', b -> b.color(TextColor.color(85, 85, 85))),
        LIGHT_BLUE('9', b -> b.color(TextColor.color(85, 85, 255))),
        LIGHT_GREEN('a', b -> b.color(TextColor.color(85, 255, 85))),
        LIGHT_CYAN('b', b -> b.color(TextColor.color(85, 255, 255))),
        LIGHT_RED('c', b -> b.color(TextColor.color(255, 85, 85))),
        MAGENTA('d', b -> b.color(TextColor.color(255, 85, 255))),
        YELLOW('e', b -> b.color(TextColor.color(255, 255, 85))),
        WHITE('f', b -> b.color(TextColor.color(255, 255, 255)));

        ColorCode(Character c, Consumer<Style.Builder> style) {
            put(c, style);
        }

        private static final Map<Character, Consumer<Style.Builder>> table = new HashMap<>();

        private void put(Character c, Consumer<Style.Builder> style) {
            table.put(c, style);
        }

        public static Consumer<Style.Builder> get(Character c) {
            return table.get(c);
        }
    }

    public enum StyleCode {
        OBFUSCATED('k', b -> b.decoration(TextDecoration.OBFUSCATED, true)),
        BOLD('l', b -> b.decoration(TextDecoration.BOLD, true)),
        STRIKETHROUGH('m', b -> b.decoration(TextDecoration.STRIKETHROUGH, true)),
        DECORATION('n', b -> b.decoration(TextDecoration.UNDERLINED, true)),
        ITALIC('o', b -> b.decoration(TextDecoration.ITALIC, true));

        StyleCode(Character c, Consumer<Style.Builder> style) {
            put(c, style);
        }

        private static final Map<Character, Consumer<Style.Builder>> table = new HashMap<>();

        private void put(Character c, Consumer<Style.Builder> style) {
            table.put(c, style);
        }

        public static Consumer<Style.Builder> get(Character c) {
            return table.get(c);
        }
    }
}
