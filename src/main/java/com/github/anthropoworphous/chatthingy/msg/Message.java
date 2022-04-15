package com.github.anthropoworphous.chatthingy.msg;

import com.github.anthropoworphous.chatthingy.msg.text.Text;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.reader.Readers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class Message {
    public Message(User sender, @NotNull Text<?, ?> content, Set<Readers> readers) {
        this.content = content;
        this.sender = sender;
        this.readers = readers;
    }
    public Message(User sender, @NotNull Text<?, ?> content, Readers readers) {
        this.content = content;
        this.sender = sender;
        this.readers = Collections.singleton(readers);
    }

    private final Text<?, ?> content;
    private final User sender;
    private final Set<Readers> readers;

    public Text<?, ?> getRawContent() { return content; }

    public <T extends Text<?, ?>> T content(Class<T> textType) {
        return textType.cast(content);
    }

    @Contract("-> !null")
    public User getSender() {
        return sender;
    }

    @Contract("-> !null")
    public Set<Readers> getReaders() {
        return readers;
    }
}
