package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.impl;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.minecraft.Private;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector.ChannelSelector;
import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@ChannelSelector.Modifier(varSize = 1)
public class SendToPrivateChat implements ChannelSelector {
    private static final SendToPrivateChat instance = new SendToPrivateChat();

    private SendToPrivateChat() {}

    public static SendToPrivateChat get() { return instance; }

    @Override
    public Channel channel() {
        return Private.channel();
    }

    @Override
    public void resolve(Message msg, List<IWord> var) throws Exception {
        Player p = Optional.ofNullable(Bukkit.getPlayerExact(var.get(0).text()))
                .orElseThrow(() -> new Exception(
                        "Player %s not found".formatted(var.get(0).text())
                ));
        msg.readers().clear();
        msg.readers().add(new PlayerUser(p));
        Private.channel().apply(msg);
    }
}
