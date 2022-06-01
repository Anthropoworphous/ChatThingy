package com.github.anthropoworphous.chatthingy.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentSpliter {
    public static List<Component> split(Component comp) {
        List<Component> result = new ArrayList<>();
        flatten(comp).forEach(c -> {
            Style style = c.style();
            for (String str : ComponentStringConverter.convert(c).split(" ")) {
                result.add(Component.text(str).style(style));
            }
        });
        return result;
    }

    private static List<Component> flatten(Component comp) {
        List<Component> child = comp.children();
        return child.size() == 0 ?
                Collections.singletonList(comp) :
                child.stream().flatMap(c -> flatten(c).stream()).toList();
    }
}
