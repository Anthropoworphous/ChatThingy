package com.github.anthropoworphous.chatthingy.msg.message;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.Task;
import com.github.anthropoworphous.chatthingy.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IMessage {
    // getters & setters
    Channel channel();

    void channel(Channel channel);

    Task task();

    Content getContent();

    List<User<?>> readers();

    User<?> sender();

    void sender(User<?> user);

    void addButton(Button... button);

    List<Button> buttons();

    class Content {
        private final String original;
        private final List<IWord> content;
        private Exception e = null;

        public Content(String original, List<? extends IWord> content) {
            this.original = original;
            this.content = new ArrayList<>(content);
        }

        public void fail(Exception e) {
            this.e = e;
        }

        public List<IWord> get() throws Exception {
            if (e != null) {
                throw e;
            }
            return content;
        }

        public String original() { return original; }

        public Optional<List<IWord>> opGet() {
            try {
                return Optional.of(get());
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }
}
