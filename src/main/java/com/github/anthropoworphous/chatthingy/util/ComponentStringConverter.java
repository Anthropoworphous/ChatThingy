package com.github.anthropoworphous.chatthingy.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentStringConverter {
    public static String convert(Component c) {
        return PlainTextComponentSerializer.plainText().serialize(c);
    }
}
