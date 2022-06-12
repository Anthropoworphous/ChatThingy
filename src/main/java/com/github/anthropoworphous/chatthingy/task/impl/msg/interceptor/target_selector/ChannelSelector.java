package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.target_selector;

import com.github.anthropoworphous.chatthingy.channel.Channel;
import com.github.anthropoworphous.chatthingy.msg.Message;
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
        int varSize = this.getClass().getAnnotation(Modifier.class).varSize();
        List<IWord> content = msg.getContent().get();
        if (!check(content, varSize)) { return; }

        //remove prefixTrigger
        content.remove(0);

        List<IWord> sub = content.subList(0, varSize);
        List<IWord> var = List.copyOf(sub);
        sub.clear();

        resolve(msg, var);
    }

    private boolean check(List<IWord> content, int varSize) throws Exception {
        if (this.getClass().getAnnotation(Modifier.class) == null) { throw new Exception("Channel not marked"); }
        return (channel().prefixTrigger().isEmpty() || content.get(0).text().equals(channel().prefixTrigger().get()))
                && (content.size() >= varSize + 1);
    }


    // abstract
    Channel channel();

    void resolve(Message msg, List<IWord> var) throws Exception;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Modifier {
        int varSize();
    }
}
