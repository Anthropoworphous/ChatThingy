package com.github.anthropoworphous.chatthingy.user;

import java.util.ArrayList;
import java.util.List;

public class ReaderCollector extends ArrayList<User<?>> {
    public ReaderCollector(User<?> reader) {
        super();
        add(reader);
    }
    public ReaderCollector(List<? extends User<?>> readers) {
        super(readers);
    }

    public ReaderCollector withAllOf(List<? extends User<?>> user) {
        addAll(user);
        return this;
    }
    public ReaderCollector with(User<?> user) {
        add(user);
        return this;
    }
}
