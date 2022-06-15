package com.github.anthropoworphous.chatthingy.msg.message;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.General;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.SendTask;
import com.github.anthropoworphous.chatthingy.task.Task;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractMessage implements IMessage {
    // final
    private final Task task;
    private final List<User<?>> readers;
    private final List<Button> buttons;
    private final Content content;
    // none final
    private User<?> sender;
    private Channel channel;
    // field end

    // constructor
    public AbstractMessage(User<?> sender, List<User<?>> readers, String original,
                           List<? extends IWord> content,
                           Interceptor[] interceptors
    ) {
        this.task = new SendTask(this, interceptors);
        this.content = new Content(original, content);
        this.readers = new ArrayList<>(readers);
        this.sender = sender;
        channel = new General();
        buttons = new ArrayList<>();
    }

    @Override public Task task() { return task; }
    @Override public Channel channel() { return channel; }
    @Override public void channel(Channel channel) { this.channel = channel; }
    @Override public Content getContent() { return content; }
    @Override public List<User<?>> readers() { return readers; }
    @Override public User<?> sender() { return sender; }
    @Override public void sender(User<?> user) { this.sender = user; }
    @Override public void addButton(Button... button) { buttons.addAll(Arrays.asList(button)); }
    @Override public List<Button> buttons() { return buttons; }
}
