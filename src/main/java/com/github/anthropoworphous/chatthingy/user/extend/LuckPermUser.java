package com.github.anthropoworphous.chatthingy.user.extend;

import com.github.anthropoworphous.chatthingy.user.impl.PlayerUser;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LuckPermUser extends PlayerUser {
    public LuckPermUser(
            @NotNull Player player,
            @NotNull User user
    ) {
        super(player);

        CachedDataManager c = user.getCachedData();
        CachedMetaData m = c.getMetaData();
        prefix = m.getPrefix();
        postfix = m.getSuffix();
    }

    private final @Nullable String prefix;
    private final @Nullable String postfix;

    @Override
    public Optional<String> prefix() {
        return Optional.ofNullable(prefix);
    }

    @Override
    public Optional<String> suffix() {
        return Optional.ofNullable(postfix);
    }
}
