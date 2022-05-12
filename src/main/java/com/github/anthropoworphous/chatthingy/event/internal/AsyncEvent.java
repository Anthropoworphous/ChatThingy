package com.github.anthropoworphous.chatthingy.event.internal;

import com.github.anthropoworphous.chatthingy.event.Event;
import com.github.anthropoworphous.chatthingy.task.Task;

import java.util.Collections;
import java.util.List;

public interface AsyncEvent extends Event {
    Task task();
    Trigger trigger();

    default Trigger await() {
        return this.await(-1);
    }
    default Trigger await(long timeout) {
        Trigger trigger = trigger();
        EventBus.add(trigger, task(), timeout);
        return trigger();
    }

    default List<AsyncEvent> link() {
        return Collections.emptyList();
    }
}
