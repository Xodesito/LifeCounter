package com.realxode.lifecounter.papi;

import com.realxode.lifecounter.LifeCounter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LifeCounterExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "lifecounter";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SirOswaldo";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        // %lifecounter_lives%
        if (params.equals("lives")) {
            return String.valueOf(LifeCounter.getInstance().getCounter().getLives(player));
        }
        return "";
    }

}