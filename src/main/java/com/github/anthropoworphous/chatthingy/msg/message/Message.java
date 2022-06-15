package com.github.anthropoworphous.chatthingy.msg.message;

import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.msg.word.extend.PureString;
import com.github.anthropoworphous.chatthingy.msg.word.impl.ComponentWord;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import com.github.anthropoworphous.chatthingy.util.ComponentSpliter;
import com.github.anthropoworphous.chatthingy.util.ComponentStringConverter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Message extends AbstractMessage {
    public Message(User<?> sender, @NotNull String content, List<User<?>> readers, Interceptor[] interceptors) {
        super(sender, readers,
                content,
                Arrays.stream(content.split("\\s"))
                        .map(PureString::new)
                        .toList(),
                interceptors);
    }
    public Message(User<?> sender, @NotNull Component content, List<User<?>> readers, Interceptor[] interceptors) {
        super(sender, readers,
                ComponentStringConverter.convert(content),
                ComponentSpliter.split(content).stream()
                        .map(ComponentWord::new)
                        .toList(),
                interceptors);
    }
    public Message(User<?> sender, @NotNull List<IWord> content, List<User<?>> readers, Interceptor[] interceptors) {
        super(sender, readers,
                content.stream().map(IWord::text).reduce("", "%1$s %2$s"::formatted),
                content,
                interceptors);
    }

    public static class Builder {
        private Interceptor[] interceptors = null;
        private User<?> sender = new EmptyUser();
        private List<IWord> wordsContent = null;
        private Component compContent = null;
        private String strContent = "";
        private List<User<?>> readers = new ReaderCollector(sender);

        public Builder interceptors(Interceptor... interceptors) {
            this.interceptors = interceptors;
            return this;
        }
        public Builder sendBy(User<?> sender) {
            this.sender = sender;
            return this;
        }
        public Builder content(List<IWord> content) {
            wordsContent = content;
            return this;
        }
        public Builder content(Component content) {
            compContent = content;
            return this;
        }
        public Builder content(String content) {
            strContent = content;
            return this;
        }
        public Builder readBy(List<User<?>> readers) {
            this.readers = readers;
            return this;
        }

        public IMessage build() {
            if (wordsContent != null) {
                return new Message(sender, wordsContent, readers, interceptors);
            } else if (compContent != null) {
                return new Message(sender, compContent, readers, interceptors);
            } else {
                return new Message(sender, strContent, readers, interceptors);
            }
        }
    }
}
