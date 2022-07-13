package com.realxode.lifecounter.counter;

import com.realxode.lifecounter.LifeCounter;
import org.bukkit.entity.Player;

public class Counter {
    private final LifeCounter plugin;

    public Counter(LifeCounter plugin) {
        this.plugin = plugin;
    }

    public int getLives(Player player) {
        return this.plugin.getStorage().getInt(player.getUniqueId() + ".lives");
    }

    public boolean isInConfig(Player player) {
        return this.plugin.getStorage().getConfiguration().contains(String.valueOf(player.getUniqueId()));
    }


    public void addLives(Player player, int lives) {
        this.plugin.getStorage().getConfiguration().set(player.getUniqueId() + ".lives", this.getLives(player) + lives);
        this.plugin.getStorage().save();
    }

    public void removeLives(Player player, int lives) {
        this.plugin.getStorage().getConfiguration().set(player.getUniqueId() + ".lives", this.getLives(player) - lives);
        this.plugin.getStorage().save();
    }

    public void setLives(Player player, int lives) {
        this.plugin.getStorage().getConfiguration().set(player.getUniqueId() + ".lives", lives);
        this.plugin.getStorage().save();
    }
}
