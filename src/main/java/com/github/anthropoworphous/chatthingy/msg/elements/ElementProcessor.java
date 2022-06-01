package com.github.anthropoworphous.chatthingy.msg.elements;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.util.ColorCodeDecoder;
import com.github.anthropoworphous.chatthingy.util.ComponentStringConverter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.BiPredicate;

public class ElementProcessor {
    private final Map<UUID, Works> works = new HashMap<>();

    public State getState(IElement e) { return getWorks(e).state(); }

    // process trigger
    public final void preprocess(Message msg, IElement[] elements, IElement e) {
        if (getState(e) == State.PENDING) {
            getWorks(e).state(State.PROCESSING);

            getWorks(e).state(getWorks(e).preprocess().test(msg, elements) && e.getStr(msg).isPresent()
                    ? State.SUCCESS
                    : State.FAILED);
        } else if (getState(e) == State.PROCESSING) {
            throw new IllegalStateException("Preprocess formed a loop");
        }
    }
    public final String strPostprocess(String result, Message msg, IElement e) {
        return getWorks(e).postprocess().runStr(msg, result, e);
    }
    public final Component compPostprocess(Component result, Message msg, IElement e) {
        return Component.text().append(getWorks(e).postprocess().runComp(msg, result, e)).build().compact();
    }

    // processes
    public IElement lookAhead(IElement e) {
        addPreprocess(e, (msg, ele) -> {
            for (int i = 0; i < ele.length; i++) {
                if (ele[i] == e) {
                    if (i+1 >= ele.length) {
                        Bukkit.getLogger().info("Out of bound LookAhead on element %s"
                                .formatted(ele[i].getStr(msg).orElse("null")));
                        return false;
                    }
                    preprocess(msg, ele, ele[i+1]); // make sure the element is ready, it'str cached anyway.
                    return getState(ele[i+1]) == State.SUCCESS;
                }
            }
            // impossible, this will only happen if the element list doesn't contain this element,
            // which just isn't possible, unless I messed up as per usual
            return false;
        });
        return e;
    }
    public IElement lookBehind(IElement e) {
        addPreprocess(e, (msg, ele) -> {
            for (int i = 0; i < ele.length; i++) {
                if (ele[i] == e) {
                    if (i-1 < 0) {
                        Bukkit.getLogger().info("Out of bound LookAhead on element %s"
                                .formatted(ele[i].getStr(msg).orElse("null")));
                        return false;
                    }
                    preprocess(msg, ele, ele[i-1]); // make sure the element is ready, it'str cached anyway.
                    return getState(ele[i-1]) == State.SUCCESS;
                }
            }
            // impossible, this will only happen if the element list doesn't contain this element,
            // which just isn't possible, unless I messed up as per usual
            return false;
        });
        return e;
    }
    public IElement decodeColorCode(IElement e) {
        addCompPostprocess(e, (result, msg, ele) ->
                ColorCodeDecoder.decode(ComponentStringConverter.convert(result)));
        return e;
    }
    public IElement elementHover(IElement e, IElement toHover) {
        addCompPostprocess(e, (result, msg, ele) -> {
            preprocess(msg, new IElement[]{toHover}, toHover);
            return toHover.getAsComponent(msg)
                    .map(result::hoverEvent).orElse(result);
            }
        );
        return e;
    }
    public IElement elementHover(IElement e, HoverEvent<?> hoverEvent) {
        addCompPostprocess(e, (result, msg, ele) -> result.hoverEvent(hoverEvent));
        return e;
    }
    public IElement elementClick(IElement e, ClickEvent clickEvent) {
        addCompPostprocess(e, (result, msg, ele) -> result.clickEvent(clickEvent));
        return e;
    }

    // process registering method
    private void addPreprocess(IElement e, BiPredicate<Message, IElement[]> process) {
        getWorks(e).preprocess().add(process);
    }
    private void addStrPostprocess(IElement e, ElementPostprocess.PostProcess<String> process) {
        getWorks(e).postprocess().addStrPostprocess(process);
    }
    private void addCompPostprocess(IElement e, ElementPostprocess.PostProcess<Component> process) {
        getWorks(e).postprocess().addCompPostprocess(process);
    }

    // helper method
    private Works getWorks(IElement e) {
        works.putIfAbsent(e.id(), new Works(e));
        return works.get(e.id());
    }

    // classes
    public static class Chain {
        private IElement e;
        private final ElementProcessor ep;

        public Chain(ElementProcessor ep, IElement e) {
            this.e = e;
            this.ep = ep;
        }

        public Chain lookAhead() {
            e = ep.lookAhead(e);
            return this;
        }
        public Chain lookBehind() {
            e = ep.lookBehind(e);
            return this;
        }
        public Chain decodeColorCode() {
            ep.decodeColorCode(e);
            return this;
        }
        public Chain elementHover(IElement toHover) {
            e = ep.elementHover(e, toHover);
            return this;
        }
        public Chain elementHover(HoverEvent<?> hoverEvent) {
            e = ep.elementHover(e, hoverEvent);
            return this;
        }
        public Chain elementClick(ClickEvent clickEvent) {
            e = ep.elementClick(e, clickEvent);
            return this;
        }

        public IElement end() { return e; }
    }

    private static class Works {
        private final IElement e;
        private final ElementPreprocess preprocess = new ElementPreprocess();
        private final ElementPostprocess postprocess = new ElementPostprocess();

        private State state = State.PENDING;

        public Works(IElement e) {
            this.e = e;
        }

        public IElement e() {
            return e;
        }
        public ElementPreprocess preprocess() {
            return preprocess;
        }
        public ElementPostprocess postprocess() {
            return postprocess;
        }
        public State state() {
            return state;
        }

        public void state(State state) {
            this.state = state;
        }
    }

    private static class ElementPreprocess {
        private final Set<BiPredicate<Message, IElement[]>> processes = new HashSet<>();

        public void add(BiPredicate<Message, IElement[]> p) {
            processes.add(p);
        }
        private boolean test(Message msg, IElement[] elements) {
            return processes.stream().allMatch(p -> p.test(msg, elements));
        }
    }
    private static class ElementPostprocess {
        private final Set<PostProcess<String>> strPostprocessors = new HashSet<>();
        private final Set<PostProcess<Component>> compPostprocessors = new HashSet<>();

        public void addStrPostprocess(PostProcess<String> p) {
            strPostprocessors.add(p);
        }
        public void addCompPostprocess(PostProcess<Component> p) {
            compPostprocessors.add(p);
        }
        private String runStr(Message msg, String result, IElement element) {
            for (PostProcess<String> p : strPostprocessors) {
                result = p.process(result, msg, element);
            }
            return result;
        }
        private Component runComp(Message msg, Component result, IElement element) {
            for (PostProcess<Component> p : compPostprocessors) {
                result = p.process(result, msg, element);
            }
            return result;
        }

        @FunctionalInterface
        public interface PostProcess<T> {
            T process(T source, Message msg, IElement element);
        }
    }
}
