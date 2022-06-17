package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.List;
import java.util.UUID;

public class EmptyUser<T> extends User<T> {
    private static final String ID = UUID.randomUUID().toString();

    public EmptyUser() {
        super(ID);
    }

    @Override
    public String pfpUrl() { return "https://mc-heads.net/avatar/false"; }

    @Override
    protected void accept(T content) { }

    @Override
    protected T read(IMessage message) { return null; }

    @Override
    protected T error(Exception e) { return null; }

    @Override
    protected T acceptButton(List<Button> buttons) { return null; }

    @Override
    public boolean checkPermission(String node) { return false; }
}
