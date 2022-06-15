package com.github.anthropoworphous.chatthingy.msg.interceptor;

import com.github.anthropoworphous.chatthingy.msg.message.IMessage;

public interface Interceptor {
    void intercept(IMessage msg) throws Exception;

    default String interceptorName() {
        return this.getClass()
                .getSimpleName()
                .replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toLowerCase();
    }
}
