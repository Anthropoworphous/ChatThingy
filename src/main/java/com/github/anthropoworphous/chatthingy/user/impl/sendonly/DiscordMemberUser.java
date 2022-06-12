package com.github.anthropoworphous.chatthingy.user.impl.sendonly;

import com.github.anthropoworphous.chatthingy.msg.Button;
import com.github.anthropoworphous.chatthingy.msg.Message;
import com.github.anthropoworphous.chatthingy.user.User;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.discordjson.json.ChannelData;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class DiscordMemberUser extends User<Object> {
    private final Member member;
    private final MessageChannel channel;

    public DiscordMemberUser(MessageChannel channel, Member member) {
        super(member.getId().toString());
        this.channel = channel;
        this.member = member;
    }

    @Override
    public boolean checkPermission(String node) {
        // TODO idk about this... might need a config to link discord rank to either luck perm or perm node
        return false;
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
    public Optional<String> suffix() {
        ChannelData restChannel = channel.getRestChannel().getData().block(Duration.ofSeconds(1));
        return restChannel == null ? Optional.of("Unknown") : restChannel.name().toOptional();
    }

    // send only, ignore all these
    @Override
    protected void accept(Object content) {}
    @Override
    protected Object read(Message message) { return null; }
    @Override
    protected Object error(Exception e) { return null; }
    @Override
    protected Object acceptButton(List<Button> buttons) { return null; }
}
