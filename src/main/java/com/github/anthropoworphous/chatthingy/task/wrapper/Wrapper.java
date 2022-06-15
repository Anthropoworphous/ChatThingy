package com.github.anthropoworphous.chatthingy.task.wrapper;

import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.task.SendTask;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface Wrapper {
    Set<Wrapper> wrappers = ConcurrentHashMap.newKeySet();

    static void wrapping(User<?> user) {
        wrappers.forEach(w -> w.wrap(user));
    }
    static void wrapping(IMessage msg) {
        wrappers.forEach(w -> w.wrap(msg));
    }
    static void wrapping(SendTask sendTask) {
        wrappers.forEach(w -> w.wrap(sendTask));
    }

    default void wrap(User<?> user) {}
    default void wrap(IMessage msg) {}
    default void wrap(SendTask sendTask) {}

    default void register() {
        wrappers.add(this);
    }
}
