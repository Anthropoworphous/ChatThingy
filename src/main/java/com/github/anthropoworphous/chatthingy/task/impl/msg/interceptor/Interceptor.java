package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor;

import com.github.anthropoworphous.chatthingy.msg.Message;

public interface Interceptor {
    void intercept(Message msg) throws Exception;

    default String interceptorName() {
        return this.getClass()
                .getSimpleName()
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }
}
