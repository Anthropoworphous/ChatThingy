package com.github.anthropoworphous.chatthingy.event.internal;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.data.cache.Cache;
import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.ListCache;
import com.github.anthropoworphous.chatthingy.task.Task;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EventBus {
    private static final Cache<Trigger, Task> bus = new RandomCache<>();
    private static final ListCache<Trigger, Trigger, ArrayList<Trigger>> links
            = new ListCache<>(RandomCache::new, ArrayList::new);

    public static void add(AsyncEvent event, long timeout) {
        link(event, new ArrayList<>());
        add(event.trigger(), event.task(), timeout);
    }
    public static void add(Trigger trigger, Task task, long timeout) {
        bus.put(trigger, task);
        if (timeout > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    bus.remove(trigger);
                }
            }.runTaskLater(ChatThingy.plugin(), timeout);
        }
    }

    public static boolean exist(Trigger trigger) {
        return bus.containsKey(trigger);
    }
    public static boolean exist(String trigger) {
        return bus.containsKey(new Trigger(trigger));
    }

    /**
     * READONLY
     * @return the eventbus' content
     */
    public static Map<Trigger, Task> bus() {
        return Collections.unmodifiableMap(bus);
    }

    public static boolean trigger(Trigger trigger) {
        Optional<Task> task = Optional.ofNullable(bus.get(trigger));
        if (task.isEmpty()) { return false; }

        try {
            task.get().run();
            tearDownLink(trigger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean trigger(String trigger) {
        return trigger(new Trigger(trigger));
    }

    private static void link(AsyncEvent event, List<AsyncEvent> blacklist) {
        blacklist.add(event);
        for (AsyncEvent e : event.link()) {
            if (blacklist.contains(e) || e == event) {
                continue;
            }
            links.add(event.trigger(), e.trigger());
            link(e, blacklist);
        }
    }
    private static void tearDownLink(Trigger trigger) {
        links.opGet(trigger)
                .ifPresent(l -> l.forEach(EventBus::tearDownLink));
        links.remove(trigger); // It doesn't matter if the key doesn't exist
    }
}
