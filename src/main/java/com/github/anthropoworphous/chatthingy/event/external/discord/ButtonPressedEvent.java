package com.github.anthropoworphous.chatthingy.event.external.discord;

import com.github.anthropoworphous.chatthingy.event.internal.EventBus;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import reactor.core.publisher.Mono;

public class ButtonPressedEvent implements DiscordEvent {
    @Override
    public void init(GatewayDiscordClient client) {
        client.getEventDispatcher().on(ButtonInteractionEvent.class, event -> {
            if (!EventBus.exist(event.getCustomId())) {
                return event.createFollowup("Button timed out");
            }
            EventBus.trigger(event.getCustomId());
            return Mono.empty();
        }).subscribe();
    }
}
