package com.github.anthropoworphous.chatthingy.task;

import com.github.anthropoworphous.chatthingy.msg.interceptor.HaltMessage;
import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.task.wrapper.Wrapper;

public class SendTask implements Task {
    public SendTask(IMessage msg, Interceptor... interceptors) {
        this.msg = msg;
        this.interceptors = interceptors;

        Wrapper.wrapping(this);
    }

    private final IMessage msg;
    private final Interceptor[] interceptors; // TODO turn interceptor into config controlled instead of hard coded

    public Interceptor[] interceptors() {
        return interceptors;
    }

    @Override
    public void run() {
        Wrapper.wrapping(msg);

        for (Interceptor interceptor : interceptors) {
            try {
                interceptor.intercept(msg);
            } catch (HaltMessage halt) {
                halt.response().run();
                return;
            } catch (Exception e) {
                msg.sender().accept(e);
                return;
            }
        }

        msg.readers().stream()
                .parallel() // synchronous
                .forEach(reader -> {
                    reader.accept(msg);
                    if (msg.buttons().size() > 0) {
                        reader.accept(msg.buttons());
                    }
                });
    }
}