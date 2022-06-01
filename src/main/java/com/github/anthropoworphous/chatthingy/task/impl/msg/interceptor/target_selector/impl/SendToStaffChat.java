package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Staff;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.ChannelSelector;

import java.util.List;

@ChannelSelector.Modifier(varSize = 0)
public class SendToStaffChat implements ChannelSelector {
    @Override
    public Channel channel() {
        return new Staff();
    }

    @Override
    public void resolve(Message msg, List<IWord> var) throws Exception {
        channel().apply(msg);
    }
}
