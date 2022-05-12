package com.github.anthropoworphous.chatthingy.msg;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.channel.impl.General;
import com.github.anthropoworphous.chatthingy.msg.elements.ElementProcessor;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.word.extend.PureString;
import com.github.anthropoworphous.chatthingy.msg.word.impl.ComponentWord;
import com.github.anthropoworphous.chatthingy.task.Task;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.util.ComponentSpliter;
import com.github.anthropoworphous.chatthingy.util.ComponentStringConverter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Message {
    private Channel channel = General.channel();
    private final String originalContent;
    private final Content content;
    private ElementProcessor ep = null; // CAN NOT be used outside of Reader.read()
    private Task task;
    private User<?> sender;
    private final List<User<?>> readers;
    private final List<Button> buttons = new ArrayList<>();
    private final List<String> tags = new ArrayList<>();

    public Message(User<?> sender, @NotNull String content, List<User<?>> readers) {
        originalContent = content;
        this.content = new Content(new ArrayList<>(
                Arrays.stream(content.split("\s"))
                        .map(PureString::new)
                        .toList()
        ));
        this.sender = sender;
        this.readers = readers;
    }
    public Message(User<?> sender, @NotNull Component content, List<User<?>> readers) {
        originalContent = ComponentStringConverter.convert(content);
        this.content = new Content(new ArrayList<>(
                ComponentSpliter.split(content).stream()
                        .map(ComponentWord::new)
                        .toList()
        ));
        this.sender = sender;
        this.readers = readers;
    }
    public Message(User<?> sender, @NotNull List<IWord> content, List<User<?>> readers) {
        originalContent = content.stream().map(IWord::text).reduce("", "%1$str %2$str"::formatted);
        this.content = new Content(new ArrayList<>(content));
        this.sender = sender;
        this.readers = readers;
    }

    // getters & setters
    public Channel channel() { return channel; }
    public void channel(Channel channel) { this.channel = channel; }

    public void provideReference(Task task) {
        this.task = task;
    }
    public Task task() {
        return task;
    }

    public void assignElementProcessor(ElementProcessor ep) {
        this.ep = ep;
    }
    public ElementProcessor getEP() { return ep; }

    public String getOriginalContent() { return originalContent; }
    public Content getContent() { return content; }

    public List<User<?>> readers() {
        return readers;
    }

    public User<?> sender() {
        return sender;
    }
    public void sender(User<?> user) {
        sender = user;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
    public List<Button> buttons() { return buttons; }

    public void addTag(String tag) { tags.add(tag); }
    public void removeTag(String tag) { tags.remove(tag); }
    public boolean hasTag(String tag) { return tags.contains(tag); }

    public static class Content {
        private final List<IWord> content;
        private Exception e = null;

        public Content(List<IWord> content) {
            this.content = new ArrayList<>(content);
        }

        public void fail(Exception e) {
            this.e = e;
        }

        public List<IWord> get() throws Exception {
            if (e != null) { throw e; }
            return content;
        }

        public Optional<List<IWord>> opGet() {
            try {
                return Optional.of(get());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
