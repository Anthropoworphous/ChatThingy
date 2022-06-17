package com.github.anthropoworphous.chatthingy.cmd.discord;

import com.github.anthropoworphous.chatthingy.ChatThingy;
import com.github.anthropoworphous.chatthingy.error.handling.ExceptionHandler;
import com.github.anthropoworphous.chatthingy.event.external.discord.DiscordEvent;
import com.github.anthropoworphous.chatthingy.hook.DiscordHook;
import com.github.anthropoworphous.chatthingy.log.Logger;
import com.github.anthropoworphous.chatthingy.user.extend.DiscordMemberUser;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.PartialMember;
import discord4j.core.spec.EmbedCreateSpec;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CommandProcessor implements DiscordEvent {
    private static final Logger logger = new Logger();
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
        } catch (IllegalArgumentException e) { return -2; }
    }

    @SuppressWarnings("unused")
    private enum CMD {
        IGNORE(-1) {
            @Override
            void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {}
        },
        RUN(-1) {
            @Override
            void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {
                Optional<Member> disSender = event.getMember();
                boolean op = disSender
                        .flatMap(m -> m.getBasePermissions().blockOptional(Duration.ofSeconds(1)))
                        .map(perms -> perms.contains(discord4j.rest.util.Permission.ADMINISTRATOR))
                        .orElse(false);
                BufferedCommandSender cmdSender = new RemoteMinecraftCommandExecutor.Builder()
                        .name(disSender.map(PartialMember::getDisplayName).orElse("Anonymous"))
                        .isOp(op)
                        .build();
                try {
                    Bukkit.getScheduler().callSyncMethod(
                            ChatThingy.plugin(),
                            () -> Bukkit.dispatchCommand(cmdSender, String.join(" ", cmd))
                    ).get();
                    cmdSender.sendMessage("[RUN] Command executed");
                } catch (InterruptedException e) {
                    cmdSender.sendMessage("Command execution interrupted");
                } catch (ExecutionException e) {
                    event.getMember().ifPresent(member ->
                            new DiscordMemberUser(
                                    event.getMessage().getChannel().block(Duration.ofSeconds(10)),
                                    member
                            ).accept(e)
                    );
                }

                Optional.ofNullable(event.getMessage()
                        .getChannel()
                        .block(Duration.ofSeconds(10))
                ).ifPresentOrElse(
                        c -> c.createMessage(EmbedCreateSpec.builder()
                                .author("console", null, "https://mc-heads.net/avatar/console")
                                .addField("result (might be empty)", cmdSender.getBuffer(), false)
                                .build()
                        ).subscribe(),
                        () -> logger.log("Unable to fetch discord channel for some reason"));
            }
        },
        LINK(0) {
            @Override
            void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd) {
                Optional.ofNullable(event.getMessage()
                                .getChannel()
                                .block(Duration.ofSeconds(10))
                ).ifPresentOrElse(
                        c -> hook.linkChannel(c.getId().asString()),
                        () -> logger.log("Unable to fetch discord channel for some reason"));
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
            if (argSize != -1 && cmd.size() != argSize) { return -3; }
            try {
                p(event, hook, cmd);
            } catch (Exception e) {
                new ExceptionHandler().handle(e);
            }
            return 0;
        }

        abstract void p(MessageCreateEvent event, DiscordHook hook, List<String> cmd);
    }

    private static class RemoteMinecraftCommandExecutor extends BufferedCommandSender implements RemoteConsoleCommandSender {
        private final String name;
        private boolean isOp;
        private final List<String> permissions;

        public RemoteMinecraftCommandExecutor(String name, boolean isOp, List<String> permissions) {
            this.name = name;
            this.isOp = isOp;
            this.permissions = permissions;
        }

        public static class Builder {
            private String name;
            private boolean isOp;
            private final List<String> permissions;

            public Builder() {
                name = "ChatThingy-RemoteMinecraftCommandExecutor";
                isOp = false;
                this.permissions = new ArrayList<>(
                        List.of(
                                "bukkit.command.version",
                                "bukkit.command.help",
                                "minecraft.command.help",
                                "minecraft.command.list",
                                "minecraft.command.me",
                                "minecraft.command.msg"
                        )
                );
            }

            public Builder name(String name) { this.name = name; return this; }
            public Builder isOp(boolean isOp) { this.isOp = isOp; return this; }
            public Builder permissions(List<String> permissions) { this.permissions.addAll(permissions); return this; }

            public RemoteMinecraftCommandExecutor build() {
                return new RemoteMinecraftCommandExecutor(name, isOp, permissions);
            }
        }

        @Override
        public @NotNull String getName() { return name; }
        @Override
        public @NotNull Component name() { return Component.text(name); }
        @Override
        public boolean isOp() { return isOp; }
        @Override
        public void setOp(boolean value) { isOp = value; }
        @Override
        public boolean isPermissionSet(@NotNull String name) { return permissions.contains(name); }
        @Override
        public boolean isPermissionSet(@NotNull Permission perm) { return permissions.contains(perm.getName()); }
        @Override
        public boolean hasPermission(@NotNull String name) { return isOp || permissions.contains(name); }
        @Override
        public boolean hasPermission(@NotNull Permission perm) { return isOp || permissions.contains(perm.getName()); }
        @Override
        public void recalculatePermissions() { if (isOp) { Bukkit.getConsoleSender().recalculatePermissions(); } }
        @Override
        public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
            if (isOp) { return Bukkit.getConsoleSender().getEffectivePermissions(); }
            return permissions.stream()
                    .map(str -> new PermissionAttachmentInfo(this, str, null, true))
                    .collect(Collectors.toSet());
        }
    }
}
