package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.SortedCache;
import com.github.anthropoworphous.chatthingy.data.config.interceptor.InterceptorConfig;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.user.User;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class SpamFilter implements Interceptor {
    private static final SortedCache<Instant, SendRecord> timeRecords = new SortedCache<>(100);
    private static final RandomCache<String, SendRecord> senderRecords = new RandomCache<>();
    private static final SpamFilter instance = new SpamFilter();

    private SpamFilter() {}

    public static SpamFilter get() { return instance; }

    @Override
    public void intercept(Message msg) throws Exception {
        if (Arrays.stream(InterceptorConfig.loadOr(
                this,
                c -> c.parse(interceptorName(), "bypass", str -> str.split(","))
                , new String[]{}))
                .noneMatch(str -> Optional.ofNullable(Bukkit.getPlayerUniqueId(str))
                        .map(id -> id.toString().equals(msg.sender().id()))
                        .orElse(false)
                ))
        {
            SendRecord newSendRecord = new SendRecord(msg);
            senderRecords.putIfAbsentOrPossibleErrorCompute(
                    msg.sender().id(),
                    newSendRecord,
                    (v, ec) -> {
                        try { v.update(newSendRecord); return ec.carry(v); }
                        catch (Exception e) { return ec.carry(e); }
                    });
        }
    }

    private static class SendRecord {
        private int score = 100;
        private final User<?> sender;
        private String msg;
        private Instant time;

        private SendRecord(Message msg) {
            this.sender = msg.sender();
            this.msg = msg.getContent().opGet().map(list ->
                    list.stream().collect(
                            StringBuilder::new,
                            (b, w) -> b.append(w.text().replaceAll("[^A-z\\d]", "")),
                            (b1, b2) -> b1.append(b2.toString())
                    ).toString()
            ).orElseThrow();
            this.time = new Date().toInstant();
        }

        public String msg() {
            return msg;
        }
        public Instant time() {
            return time;
        }
        public User<?> sender() {
            return sender;
        }

        public void update(SendRecord sendRecord) throws Exception {
            int strDiff = Math.min(0, LevenshteinDistance.getDefaultInstance().apply(sendRecord.msg, msg) - 10);
            int timeDiff = (int) Math.min(5, sendRecord.time.until(time, ChronoUnit.SECONDS));
            int threshold = InterceptorConfig.loadOr(
                    SpamFilter.get(),
                    c -> c.parse(SpamFilter.get().interceptorName(), "threshold", str -> {
                        try { return Integer.parseInt(str); } catch(Exception e) { return null; }
                    }),
                    15);
            int score = 2*(timeDiff^3 + 3*strDiff - threshold);
            this.score = score > 0 ? 100 : this.score + score;

            time = sendRecord.time;
            msg = sendRecord.msg;

            if (this.score < 0) {
                throw new Exception("You're sending messages too fast!");
            }
        }
    }
}
