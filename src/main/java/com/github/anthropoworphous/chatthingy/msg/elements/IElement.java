package com.github.anthropoworphous.chatthingy.msg.elements;

import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import java.util.Optional;
import java.util.UUID;

public interface IElement {
    /**
     * syntactic sugar, same as:
     * {@code new ElementProcessor.Chain(ep, IElement.?)}
     * @param ep The ep provided during MessageAdaptor read process
     * @return new Chain bound to the provided ElementProcessor and this element
     */
    default ElementProcessor.Chain ep(ElementProcessor ep) {
        return new ElementProcessor.Chain(ep, this);
    }

    UUID id();

    Optional<Style> style();
    void style(Style style);

    Optional<Component> getComp(IMessage msg);
    Optional<String> getStr(IMessage msg);

    Optional<String> getAsString(IMessage msg, ElementProcessor ep);
    Optional<Component> getAsComponent(IMessage msg, ElementProcessor ep);
}