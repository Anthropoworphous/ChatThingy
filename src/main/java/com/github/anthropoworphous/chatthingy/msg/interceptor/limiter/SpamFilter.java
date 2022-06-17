package com.github.anthropoworphous.chatthingy.msg.interceptor.limiter;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.data.cache.RandomCache;
import com.github.anthropoworphous.chatthingy.data.cache.SortedCache;
import com.github.anthropoworphous.chatthingy.data.config.BukkitConfiguration;
import com.github.anthropoworphous.chatthingy.data.config.Configuration;
import com.github.anthropoworphous.chatthingy.log.Logger;
import com.github.anthropoworphous.chatthingy.msg.interceptor.Interceptor;
import com.github.anthropoworphous.chatthingy.msg.message.IMessage;
import com.github.anthropoworphous.chatthingy.user.User;
import com.github.anthropoworphous.chatthingy.error.handling.throwable.Throwable;
import org.ini4j.Ini;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SpamFilter extends Configuration.Configured implements Interceptor {
    private static final Logger logger = new Logger();
    private static final int defaultThreshold = 1000;
    private static final int defaultCooldown = 3000;
    private static final SortedCache<Instant, SendRecord> timeRecords = new SortedCache<>(100);
    private static final RandomCache<String, SendRecord> senderRecords = new RandomCache<>();

    @Override
    public void intercept(IMessage msg) throws Exception {
        if (msg.sender().checkPermission(config().get("bypass", "permission"))) { return; }
        SendRecord newSendRecord = new SendRecord(msg);
        senderRecords.putIfAbsentOrThrowableCompute(
                msg.sender().id(),
                newSendRecord,
                (v, ec) -> {
                    try { v.update(newSendRecord); return ec.carry(v); }
                    catch (Exception e) { return ec.carry(e); }
                });
    }

    @Override
    protected Configuration generateConfig() {
        return new BukkitConfiguration.Builder()
                .name("spam-filter")
                .folder(f -> new File(f, "interceptor"))
                .defaultIniCreator(() -> {
                    Ini ini = new Ini();
                    ini.putComment("bypass",
                            "bypass permission allow people who have it to not get affected by it");
                    ini.put("bypass", "permission", ChatThingy.Permissions.IGNORE_SPAM_FILTER.node);
                    ini.putComment("cooldown", "time required to wait between message (mill sec)");
                    ini.put("cooldown", "for ms", "3000");
                    ini.putComment("limit", "A margin for breaking cool down limit (mill sec)");
                    ini.put("limit", "threshold", "3000");
                    return ini;
                }).build();
    }

    private static class SendRecord {
        private int score;
        private final User<?> sender;
        private Instant time;

        public SendRecord(IMessage msg) {
            sender = msg.sender();
            time = new Date().toInstant();
            score = getThreshold();
        }

        public Instant time() { return time; }
        public User<?> sender() { return sender; }

        public void update(SendRecord newerRecord) throws Exception {
            int threshold;
            int cooldown;
            try {
                threshold = getThreshold();
                cooldown = getCoolDownLimit();
            } catch (NumberFormatException e) {
                threshold = defaultThreshold;
                cooldown = defaultCooldown;
                logger.log("Invalid number format for SpamFilter's threshold");
            }
            int timeDiff = (int) Math.min(time.until(newerRecord.time(), ChronoUnit.MILLIS), Integer.MAX_VALUE);
            score = timeDiff > cooldown ? threshold : score - timeDiff;
            time = newerRecord.time;

            if (this.score < 0) {
                throw new Exception("You're sending messages too fast!");
            }
        }

        private int getThreshold() {
            return new Throwable.Result<Integer>()
                    .attempt(() -> Integer.parseInt(
                            config().get("limit", "threshold")),
                            defaultThreshold);
        }
        private int getCoolDownLimit() {
            return new Throwable.Result<Integer>()
                    .attempt(() -> Integer.parseInt(
                            config().get("cool down", "for ms")),
                            defaultCooldown);
        }
    }
}
