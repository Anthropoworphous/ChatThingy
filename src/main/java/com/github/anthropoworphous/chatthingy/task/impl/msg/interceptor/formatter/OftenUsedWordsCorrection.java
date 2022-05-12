package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.formatter;

import com.github.anthropoworphous.chatthingy.data.cache.Cache;
import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.TimedCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.ListCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.MapCache;
import com.github.anthropoworphous.chatthingy.data.cache.complex.PersistentCache;
import com.github.anthropoworphous.chatthingy.data.key.TimeKey;
import com.github.anthropoworphous.chatthingy.event.internal.AsyncEvent;
import com.github.anthropoworphous.chatthingy.event.internal.EventBus;
import com.github.anthropoworphous.chatthingy.event.internal.Trigger;
import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.msg.word.IWord;
import com.github.anthropoworphous.chatthingy.task.Task;
import com.github.anthropoworphous.chatthingy.task.impl.msg.SendTask;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.HaltMessage;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.contant.AddButton;
import com.github.anthropoworphous.chatthingy.user.ReaderCollector;
import com.github.anthropoworphous.chatthingy.user.impl.EmptyUser;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public class OftenUsedWordsCorrection implements Interceptor {
    private static final int maxStringDiff = 3;
    private static final int maxSameSoundexWords = 25;
    private static final MapCache<TimeKey, String, List<UsedString>> recentlyUsedWordsCache =
            new MapCache<>(
                    3000,
                    TimedCache::new,
                    key -> new ListCache<>(RandomCache::new, ArrayList::new)
            );
    private static final PersistentCache<String, ArrayList<UsedString>> mostUsedWordsCache = new PersistentCache<>(
            1000, "MostUsedWordsCache", () -> new ListCache<>(RandomCache::new, ArrayList::new)
    );

    @Override
    public void intercept(Message msg) throws Exception {
        Map<String, String> replacement = new HashMap<>();
        for (IWord w : msg.getContent().get()) {
            String str = w.text().toLowerCase();
            if (str.length() < 4 || !str.matches("[A-z]+")) { continue; }
            String soundex = soundexOf(str);

            Optional<ArrayList<UsedString>> search = mostUsedWordsCache.opGet(soundex);

            if (search.isPresent()) {
                UsedString min = null;
                int lastDiff = 0;
                for (UsedString potentialMatch : search.get()) {
                    if (min == null) {
                        min = potentialMatch;
                        lastDiff = LevenshteinDistance.getDefaultInstance().apply(str, min.str());
                    } else {
                        int newDiff = LevenshteinDistance.getDefaultInstance().apply(str, potentialMatch.str());
                        if (lastDiff < newDiff) {
                            min = potentialMatch;
                            lastDiff = newDiff;
                        }
                        if (newDiff == 0) { break; }
                    }
                }
                if (min == null) { continue; }
                if (lastDiff > maxStringDiff) {
                    continue;
                }
                if (lastDiff == 0) {
                    min.used();
                }
                replacement.put(str, min.str());
            } else {
                processRecentCache(soundex, str);
            }
        }

        if (replacement.size() > 0) {
            throw new HaltMessage(haltForCorrection(msg, replacement));
        }
    }

    // helper
    private static void processRecentCache(String soundex, String str) {
        for (Cache<String, List<UsedString>> cache : TimedCache.from(recentlyUsedWordsCache.cache()).get()) {
            for (var entry : cache.entrySet()) {
                if (!entry.getKey().equals(soundex)) { continue; }
                for (UsedString used : entry.getValue()) {
                    if (!used.str().equals(str)) { continue; }
                    used.used();
                    overriding(used);
                    return;
                }
                entry.getValue().add(new UsedString(str));
                return;
            }
            ListCache.from(cache).add(soundex, new UsedString(str));
        }
        ListCache.from(mostUsedWordsCache.cache()).add(soundex, new UsedString(str));
    }

    private static void overriding(UsedString newWord) {
        // check if current temp word is used more than the least used perm
        String lastKey = mostUsedWordsCache.removedTarget();
        if (lastKey == null) {
            return;
        }
        List<UsedString> list = mostUsedWordsCache.get(lastKey);
        if (list.size() < maxSameSoundexWords) {
            list.add(newWord);
        }

        int i = 0, index = 0, min = 0;
        for (UsedString word : list) {
            if (min < word.uses()) {
                min = word.uses();
                index = i;
            }
            i++;
        }
        if (newWord.uses() > min) {
            list.set(index, newWord);
        }
    }

    private static SendTask haltForCorrection(Message msg, Map<String, String> replacement) {
        AsyncEvent e1 = new AsyncEvent() {
            @Override
            public Task task() {
                return new SendTask(
                        new Message(
                                msg.sender(),
                                msg.getOriginalContent(),
                                new ReaderCollector(msg.readers())
                        ),
                        Stream.concat(
                                Arrays.stream(((SendTask) msg.task()).interceptors())
                                        .filter(i -> !(i instanceof OftenUsedWordsCorrection)),
                                Stream.of(new ReplaceWord(replacement)))
                                .toArray(Interceptor[]::new)
                );
            }

            @Override
            public Trigger trigger() {
                return Trigger.thatIs()
                        .from("haltForCorrection")
                        .event(this)
                        .recipient(msg.sender())
                        .build();
            }

            @Override
            public String eventName() {
                return "CommitCorrection";
            }
        };
        AsyncEvent e2 = new AsyncEvent() {
            @Override
            public Task task() {
                return new SendTask(
                        new Message(
                                msg.sender(),
                                msg.getOriginalContent(),
                                new ReaderCollector(msg.readers())
                        ),
                        Arrays.stream(((SendTask) msg.task()).interceptors())
                                .filter(i -> !(i instanceof OftenUsedWordsCorrection))
                                .toArray(Interceptor[]::new)
                );
            }

            @Override
            public Trigger trigger() {
                return Trigger.thatIs()
                        .from("haltForCorrection")
                        .event(this)
                        .recipient(msg.sender())
                        .build();
            }

            @Override
            public String eventName() {
                return "ignoreCorrection";
            }

            @Override
            public List<AsyncEvent> link() {
                return List.of(e1);
            }
        };

        EventBus.add(e1, 30000);
        EventBus.add(e2, 30000);

        return new SendTask(new Message(
                new EmptyUser(),
                replacement.entrySet().stream()
                        .collect(StringBuilder::new,
                                (sb, entry) -> sb.append(entry.getKey())
                                        .append(" -> ")
                                        .append(entry.getValue())
                                        .append(", "),
                                (sb1, sb2) -> sb1.append(sb2.toString())
                        ).toString(),
                new ReaderCollector(msg.sender())
        ), new AddButton(
                new Button("Accept", e1.trigger(), NamedTextColor.GREEN),
                new Button("Ignore", e2.trigger(), NamedTextColor.RED)
        ));
    }

    private String soundexOf(String str) {
        return str.replaceAll("[aeiouyhw]", "")
                .replaceAll("[bfpv]", "1")
                .replaceAll("[cgjkqsxz]", "2")
                .replaceAll("[dt]", "3")
                .replaceAll("l", "4")
                .replaceAll("[mn]", "5")
                .replaceAll("r", "6");
    }

    // data class
    public static class UsedString implements Serializable {
        private final String str;
        private int count = 0;

        public UsedString(String str) {
            this.str = str;
        }

        public void used() { count++; }
        public int uses() { return count; }

        public String str() {
            return str;
        }
    }
}
