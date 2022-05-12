package com.github.anthropoworphous.chatthingy.msg.elements;

import com.github.anthropoworphous.chatthingy.msg.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import java.util.Optional;
import java.util.UUID;

public interface IElement {
    /**
     * syntactic sugar, same as:
     * {@code <Code>new ElementProcessor.Chain(ep, IElement.?)</Code>}
     * @param ep The ep provided during MessageAdaptor read process
     * @return new Chain bound to the provided ElementProcessor and this element
     */
    default ElementProcessor.Chain ep(ElementProcessor ep) {
        return new ElementProcessor.Chain(ep, this);
    }

    UUID id();

    Optional<Style> style();
    void style(Style style);

    Optional<Component> getComp(Message msg);
    Optional<String> getStr(Message msg);

    Optional<String> getAsString(Message msg);
    Optional<Component> getAsComponent(Message msg);
}