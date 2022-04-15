package com.github.anthropoworphous.chatthingy.task.interceptor;

import com.github.anthropoworphous.chatthingy.msg.Message;

public interface Interceptor {
    Message intercept(Message msg);
}
