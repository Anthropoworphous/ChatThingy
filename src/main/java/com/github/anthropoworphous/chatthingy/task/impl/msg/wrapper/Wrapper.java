package com.github.anthropoworphous.chatthingy.task.impl.msg.wrapper;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface Wrapper {
    Set<Wrapper> wrappers = ConcurrentHashMap.newKeySet();

    static void wrapping(User<?> user) {
        wrappers.forEach(w -> w.wrap(user));
    }
    static void wrapping(Message msg) {
        wrappers.forEach(w -> w.wrap(msg));
    }
    static void wrapping(SendTask sendTask) {
        wrappers.forEach(w -> w.wrap(sendTask));
    }

    default void wrap(User<?> user) {}
    default void wrap(Message msg) {}
    default void wrap(SendTask sendTask) {}

    default void register() {
        wrappers.add(this);
    }
}
