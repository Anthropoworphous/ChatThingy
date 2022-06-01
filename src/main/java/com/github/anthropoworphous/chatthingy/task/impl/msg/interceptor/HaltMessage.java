package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor;

import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;

public class HaltMessage extends RuntimeException {
    private final SendTask response;

    public HaltMessage(SendTask response) {
        this.response = response;
    }

    public SendTask response() {
        return response;
    }
}
