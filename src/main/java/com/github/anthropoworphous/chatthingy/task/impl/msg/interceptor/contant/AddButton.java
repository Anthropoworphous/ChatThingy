package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.contant;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;

public class AddButton implements Interceptor {
    private final Button[] buttons;

    public AddButton(Button... buttons) {
        this.buttons = buttons;
    }

    @Override
    public void intercept(Message msg) {
        for (Button b : buttons) {
            msg.addButton(b);
        }
    }
}
