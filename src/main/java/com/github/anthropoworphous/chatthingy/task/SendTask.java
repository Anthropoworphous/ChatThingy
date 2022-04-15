package com.github.anthropoworphous.chatthingy.task;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.Result;
import com.github.anthropoworphous.chatthingy.task.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.user.reader.Readers;

import java.util.HashMap;
import java.util.Map;

public class SendTask {
    public SendTask(Message msg, Interceptor... interceptors) {
        this.msg = msg;
        this.interceptors = interceptors;
    }

    private Message msg;
    private final Interceptor[] interceptors;
    private final Map<Readers, Result> results = new HashMap<>();


    public void run() {
        for (Interceptor interceptor : interceptors) {
            msg = interceptor.intercept(msg);
        }

        for (Readers readers : msg.getReaders()) {
            results.put(readers, readers.read(msg));
        }
    }

    public Map<Readers, Result> getResults() {
        return results;
    }
}