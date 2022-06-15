package com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.impl;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Staff;
import com.github.anthropoworphous.chatthingy.msg.interceptor.target_selector.ChannelSelector;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;

import java.util.List;

@ChannelSelector.Modifier(varSize = 0)
public class SendToStaffChat implements ChannelSelector {
    @Override
    public Channel channel() {
        return new Staff();
    }

    @Override
    public void resolve(IMessage msg, List<IWord> var) throws Exception {
        channel().apply(msg);
    }
}
