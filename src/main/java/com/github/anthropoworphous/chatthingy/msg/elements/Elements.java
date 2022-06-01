package com.github.anthropoworphous.chatthingy.msg.elements;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.util.ComponentMerger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class Elements {
    private static abstract class Base extends Element {
        private Style style = Style.empty();

        @Override
        public Optional<Style> style() {
            return Optional.of(style);
        }

        @Override
        public void style(Style style) {
            this.style = style;
        }

        @Override
        public Optional<Component> getComp(Message msg) {
            return getStr(msg).map(str -> Component.text(str)
                            .style(this.style().orElse(Style.empty())));
        }
    }

    // ------------------------------------
    // format related
    // ------------------------------------
    /**
     * @return an empty space, " "
     */
    public static IElement space() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return Optional.of(" ");
            }
        };
    }
    /**
     * @return a open bracket, "[", usually used to surround title and prefix
     */
    public static IElement openBracket() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return Optional.of("[");
            }
        };
    }
    /**
     * @return a close bracket, "]", usually used to surround title and prefix
     */
    public static IElement closeBracket() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return Optional.of("]");
            }
        };
    }
    /**
     * @return a colon and a space, ": ", usually used to separate the name and the message
     */
    public static IElement messageIndicator() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return Optional.of(": ");
            }
        };
    }
    /**
     * put a timestamp, time is based on the server, usually added at the end of the sentence
     */
    public static IElement timestamp() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return Optional.of(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        };
    }

    // ------------------------------------
    // channel related
    // ------------------------------------
    /**
     * @return the name of the channel
     */
    public static IElement channelName() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return msg.channel().name();
            }
        };
    }


    // ------------------------------------
    // message related
    // ------------------------------------
    /**
     * @return the prefix of the sender, something that they want to be addressed by
     */
    public static IElement senderPrefix() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return msg.sender().prefix();
            }
        };
    }
    /**
     * @return the name of the sender, something that the sender want to be called by
     */
    public static IElement senderName() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return msg.sender().name();
            }
        };
    }
    /**
     * @return The text that's getting send
     */
    public static IElement message() {
        return new Base() {
            @Override
            public Optional<String> getStr(Message msg) {
                return msg.getContent().opGet().map(l -> String.join(" ", l.stream().map(IWord::text).toList()));
            }
            @Override
            public Optional<Component> getComp(Message msg) {
                return msg.getContent().opGet()
                        .map(list -> ComponentMerger.merge(list.stream().map(IWord::component).toList(), " "));
            }
        };
    }
}