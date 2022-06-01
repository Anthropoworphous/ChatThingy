package com.github.anthropoworphous.chatthingy.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentMerger {
    public static Component merge(List<Component> comps) {
        return merge(comps, "");
    }
    public static Component merge(List<Component> comps, String delimiter) {
        if (comps.isEmpty()) {
            return Component.text().build();
        }

        comps = new ArrayList<>(comps);
        TextComponent.Builder result = Component.text();
        Component last = comps.remove(comps.size() - 1);
        comps.forEach(c -> result.append(c).append(Component.text(delimiter)));
        return result.append(last).build();
    }
}
