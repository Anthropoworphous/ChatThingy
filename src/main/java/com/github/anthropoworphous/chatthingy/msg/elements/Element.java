package com.github.anthropoworphous.chatthingy.msg.elements;

import com.github.anthropoworphous.chatthingy.msg.Message;
import net.kyori.adventure.text.Component;

import java.util.Optional;
import java.util.UUID;

public abstract class Element implements IElement {
    private final UUID id = UUID.randomUUID();

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public final Optional<String> getAsString(Message msg) {
        if (msg.getEP().getState(this) != State.SUCCESS) {
            return Optional.empty();
        }
        return getStr(msg).map(s -> msg.getEP().strPostprocess(s, msg, this));
    }

    @Override
    public final Optional<Component> getAsComponent(Message msg) {
        if (msg.getEP().getState(this) != State.SUCCESS) {
            return Optional.empty();
        }
        return getComp(msg).map(c -> msg.getEP().compPostprocess(c, msg, this));
    }
}
