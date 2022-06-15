package com.github.anthropoworphous.chatthingy.msg.interceptor.contant;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;

public class AddButton implements Interceptor {
    private final Button[] buttons;

    public AddButton(Button... buttons) {
        this.buttons = buttons;
    }

    @Override
    public void intercept(IMessage msg) {
        for (Button b : buttons) {
            msg.addButton(b);
        }
    }
}
