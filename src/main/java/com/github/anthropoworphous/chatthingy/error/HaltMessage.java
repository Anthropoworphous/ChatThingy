package com.github.anthropoworphous.chatthingy.error;

import com.github.anthropoworphous.chatthingy.task.Task;

public class HaltMessage extends RuntimeException {
    private final Task response;

    public HaltMessage(Task response) {
        this.response = response;
    }

    public Task response() { return response; }
}
