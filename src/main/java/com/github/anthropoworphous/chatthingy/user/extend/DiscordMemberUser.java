package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.readonly.DiscordChannelUser;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.MessageCreateSpec;

import java.time.Duration;
import java.util.Optional;

public class DiscordMemberUser extends DiscordChannelUser {
    private final Member member;

    public DiscordMemberUser(MessageChannel channel, Member member) {
        super(channel);
        this.member = member;
    }

    @Override
    public boolean checkPermission(String node) {
        // TODO idk about this... might need a config to link discord rank to either luck perm or perm node
        return false;
    }

    @Override
    public String pfpUrl() {
        return member.getAvatarUrl();
    }
    @Override
    public Optional<String> name() {
        return Optional.of(member.getDisplayName());
    }
    @Override
    public Optional<String> prefix() {
        Role role = member.getHighestRole().block(Duration.ofSeconds(1));
        return role == null ? Optional.of("Unknown") : Optional.of(role.getName());
    }

    @Override
    protected void accept(MessageCreateSpec.Builder content) {
        super.accept(content.content("<@%s>".formatted(member.getId().asString())));
    }
}
