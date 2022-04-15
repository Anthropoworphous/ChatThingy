package com.github.anthropoworphous.chatthingy.user.reader;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.Result;
import com.github.anthropoworphous.chatthingy.user.User;

/**
 * Readers should be EVERYONE that will receive the same message and not individual entity
 */
public interface Readers extends User {
    Result read(Message msg);
}
