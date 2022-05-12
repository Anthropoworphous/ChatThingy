package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector;

import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface ChannelSelector extends Interceptor {
    @Override
    default void intercept(Message msg) throws Exception {
        if (this.getClass().getAnnotation(Modifier.class) == null) { return; }

        int varSize = this.getClass().getAnnotation(Modifier.class).varSize();
        List<IWord> content = msg.getContent().get();
        if (content.size() < varSize+1) { return; }

        String first = content.get(0).text();
        if (first.length() != 1) { return; }

        char c = first.charAt(0);
        if (channel().trigger().isEmpty() || c != channel().trigger().get()) { return; }

        //remove trigger
        content.remove(0);

        List<IWord> sub = content.subList(0, varSize);
        List<IWord> var = List.copyOf(sub);
        sub.clear();

        resolve(msg, var);
    }

    Channel channel();

    void resolve(Message msg, List<IWord> var) throws Exception;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Modifier {
        int varSize();
    }
}
