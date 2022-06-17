package com.github.anthropoworphous.chatthingy.user;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;

import java.util.List;
import java.util.Optional;

public abstract class User<T> {
    // TODO maybe add an url for pfp
    private final String id;

    public User(String id) {
        this.id = id;
    }

    // accept content
    public final void accept(IMessage message) {
        accept(read(message));
    }
    public final void accept(Exception exception) {
        accept(error(exception));
    }
    public final void accept(List<Button> buttons) {
        accept(acceptButton(buttons));
    }

    protected abstract void accept(T content);
    protected abstract T read(IMessage message);
    protected abstract T error(Exception e);
    protected abstract T acceptButton(List<Button> buttons);

    // permission
    public abstract boolean checkPermission(String node);



    // user's general descriptions
    public String id() { return id; }
    public String pfpUrl() { return "https://mc-heads.net/avatar/%s".formatted(id()); }
    public Optional<String> name() { return Optional.empty(); }

    // user's detail descriptions
    public Optional<String> prefix() { return Optional.empty(); }
    public Optional<String> suffix() { return Optional.empty(); }

    // user's full descriptions
    public Optional<List<String>> bio() { return Optional.empty(); }
    public Optional<List<String>> ranks() { return Optional.empty(); }
    public Optional<List<String>> descriptions() { return Optional.empty(); }
}
