package com.github.anthropoworphous.chatthingy.event.external.discord;

import discord4j.core.GatewayDiscordClient;

public interface DiscordEvent {
    void init(GatewayDiscordClient client);
}
