package com.github.anthropoworphous.chatthingy.task.impl.msg;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.Task;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.HaltMessage;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.task.impl.msg.wrapper.Wrapper;
import org.bukkit.Bukkit;

public class SendTask implements Task {
    public SendTask(Message msg, Interceptor... interceptors) {
        msg.provideReference(this);
        this.msg = msg;
        this.interceptors = interceptors;

        Wrapper.wrapping(this);
    }

    private final Message msg;
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