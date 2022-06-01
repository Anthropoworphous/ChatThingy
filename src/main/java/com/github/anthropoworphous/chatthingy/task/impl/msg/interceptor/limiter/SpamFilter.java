package com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.SortedCache;
import com.github.anthropoworphous.chatthingy.data.config.Configured;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.task.impl.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.user.User;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.ini4j.Ini;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class SpamFilter extends Configured implements Interceptor {
    private static final SortedCache<Instant, SendRecord> timeRecords = new SortedCache<>(100);
    private static final RandomCache<String, SendRecord> senderRecords = new RandomCache<>();

    @Override
    public void intercept(Message msg) throws Exception {
        if (List.of(get("limit", "bypass", "").split(","))
                .contains(msg.sender().id()))
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

    private class SendRecord {
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
            int threshold = parse("limit", "threshold", (k1, k2) -> 15);
            int score = 2*(timeDiff^3 + 3*strDiff - threshold);
            this.score = score > 0 ? 100 : this.score + score;

            time = sendRecord.time;
            msg = sendRecord.msg;

            if (this.score < 0) {
                throw new Exception("You're sending messages too fast!");
            }
        }
    }

    @Override
    protected String configFileName() {
        return "spam-filter";
    }

    @Override
    protected File configFolder() {
        return new File(CONFIG_FOLDER, "interceptor");
    }

    @Override
    protected Ini defaultIni() {
        Ini ini = new Ini();

        ini.put("limit", "threshold", "15");
        return ini;
    }
}
