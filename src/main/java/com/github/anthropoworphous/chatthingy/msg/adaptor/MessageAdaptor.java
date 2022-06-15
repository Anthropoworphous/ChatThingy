package com.github.anthropoworphous.chatthingy.msg.adaptor;

import com.github.anthropoworphous.chatthingy.msg.elements.ElementProcessor;
import com.github.anthropoworphous.chatthingy.msg.elements.IElement;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.util.ComponentMerger;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MessageAdaptor {
    public MessageAdaptor(IMessage msg) {
        this.msg = msg;
        ep = new ElementProcessor();
    }

    private final IMessage msg;
    private final ElementProcessor ep;

    private IElement[] formatInit(IElement[] format) {
        for(IElement e : format) {
            ep.preprocess(msg, format, e);
        }

        return format;
    }

    private <A, T, F> F compile(
            IElement[] format,
            A accumulator,
            Function<IElement, Optional<T>> mapper,
            BiConsumer<A, T> accumulate,
            Function<A, F> result)
    {
        for(IElement e : format) {
            mapper.apply(e).ifPresent(m -> accumulate.accept(accumulator, m));
        }
        return result.apply(accumulator);
    }

    public String readString(Function<ElementProcessor, IElement[]> format) {
        return compile(
                formatInit(format.apply(ep)),
                new StringBuilder(),
                e -> e.getAsString(msg, ep),
                StringBuilder::append,
                StringBuilder::toString
        );
    }

    public Component readComponent(Function<ElementProcessor, IElement[]> format) {
        return compile(
                formatInit(format.apply(ep)),
                new ArrayList<Component>(),
                e -> e.getAsComponent(msg, ep),
                ArrayList::add,
                ComponentMerger::merge
        );
    }
}
