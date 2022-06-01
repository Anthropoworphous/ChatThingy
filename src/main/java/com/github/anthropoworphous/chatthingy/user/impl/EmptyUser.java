package com.github.anthropoworphous.chatthingy.user.impl;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.List;
import java.util.UUID;

public class EmptyUser extends User<Object> {
    private static final String ID = UUID.randomUUID().toString();

    public EmptyUser() {
        super(ID);
    }

    @Override
    protected void accept(Object content) { }

    @Override
    protected Object read(Message message) {
        return null;
    }

    @Override
    protected Object error(Exception e) {
        return null;
    }

    @Override
    protected Object acceptButton(List<Button> buttons) {
        return null;
    }

    @Override
    public boolean checkPermission(String node) {
        return false;
    }
}
