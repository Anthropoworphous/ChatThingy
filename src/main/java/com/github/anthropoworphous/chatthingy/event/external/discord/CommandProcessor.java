package com.github.anthropoworphous.chatthingy.event.external.discord;

import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandProcessor implements DiscordEvent {
    private static GatewayDiscordClient client = null;

    @Override
    public void init(GatewayDiscordClient client) {
        CommandProcessor.client = client;
    }

    /**
     * @param event message event
     * @param hook discordHook
     * @return result; 0 = fine, -1 = not ready, -2 = cmd not found, -3 = cmd input invalid
     */
    public static int dispatch(MessageCreateEvent event, DiscordHook hook) {
        if (client == null) { return -1; }
        String msg = event.getMessage().getContent();

        List<String> cmd = new ArrayList<>(
                Arrays.asList(msg.substring(hook.commandPrefix().length())
                        .trim()
                        .split("\\s")));

        if (cmd.size() < 1) { return -3; }

        try {
            return CMD.valueOf(cmd.remove(0).toUpperCase()).process(event, hook, cmd);
        } catch (IllegalArgumentException e) {
            return -2;
        }
    }

    @SuppressWarnings("unused")
    private enum CMD {
        LINK(0) {
            @Override
            void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {
                Optional.ofNullable(event.getMessage()
                                .getChannel()
                                .block(Duration.ofSeconds(10))
                ).ifPresentOrElse(
                        c -> hook.linkChannel(c.getId().asString()),
                        () -> Bukkit.getLogger().info("Unable to fetch discord channel for some reason"));
            }
        },
        UNLINK(0) {
            @Override
            void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {
                Optional.ofNullable(event.getMessage()
                                .getChannel()
                                .block(Duration.ofSeconds(10))
                ).ifPresentOrElse(
                        c -> hook.unlinkChannel(c.getId().asString()),
                        () -> Bukkit.getLogger().info("Unable to fetch discord channel for some reason"));
            }
        };

        private final int argSize;

        CMD(int argSize) {
            this.argSize = argSize;
        }

        /**
         * command name should be removed before this
         * @param cmd command without prefix and the command name
         * @return result; 0 = fine, -3 = input size doesn't match
         */
        public int process(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {
            if (cmd.size() != argSize) { return -3; }
            Bukkit.getLogger().info("running command: " + this.name());
            p(event, hook, cmd);
            return 0;
        }

        abstract void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd);
    }
}
