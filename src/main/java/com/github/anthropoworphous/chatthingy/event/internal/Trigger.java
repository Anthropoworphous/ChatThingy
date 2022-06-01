package com.github.anthropoworphous.chatthingy.event.internal;

import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.HashMap;
import java.util.Map;

public record Trigger(String trigger) {
    public static TriggerBuilder thatIs() {
        return new TriggerBuilder();
    }

    public static class TriggerBuilder {
        private final Map<Integer, String> triggerComponents = new HashMap<>();

        private TriggerBuilder() {}

        public TriggerBuilder from(String whoever) {
            triggerComponents.put(0, whoever);
            return this;
        }

        public TriggerBuilder event(Event event) {
            triggerComponents.put(1, event.eventName());
            return this;
        }

        public TriggerBuilder recipient(User<?> recipient) {
            triggerComponents.put(2, recipient.id());
            return this;
        }

        public Trigger build() {
            StringBuilder sb = new StringBuilder();
            for (String str : triggerComponents.values()) {
                sb.append(str.replaceAll("\\s", ";"));
            }
            return new Trigger(sb.toString());
        }
    }
}
