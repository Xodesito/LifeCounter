package com.realxode.lifecounter.counter;

import com.realxode.lifecounter.LifeCounter;
import org.bukkit.entity.Player;

public class Counter {
    private final LifeCounter plugin;

    public Counter(LifeCounter plugin) {
        this.plugin = plugin;
    }

    public int getLives(Player player) {
        return this.plugin.getCfg().getInt(player.getUniqueId() + ".LIVES");
    }

    public boolean isInConfig(Player player) {
        return this.plugin.getCfg().getConfiguration().contains(String.valueOf(player.getUniqueId()));
    }

    public void addLives(Player player, int lives) {
        this.plugin.getCfg().getConfiguration().set(player.getUniqueId() + ".LIVES", this.getLives(player) + lives);
        this.plugin.getCfg().save();
    }

    public void removeLives(Player player, int lives) {
        this.plugin.getCfg().getConfiguration().set(player.getUniqueId() + ".LIVES", this.getLives(player) - lives);
        this.plugin.getCfg().save();
    }

    public void setLives(Player player, int lives) {
        this.plugin.getCfg().getConfiguration().set(player.getUniqueId() + ".LIVES", lives);
        this.plugin.getCfg().save();
    }
}
