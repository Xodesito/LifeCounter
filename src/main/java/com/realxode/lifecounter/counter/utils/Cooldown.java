package com.realxode.lifecounter.counter.utils;

import com.realxode.lifecounter.LifeCounter;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Cooldown {

    private Instant after, before;
    private long milli;
    private int lastLive;
    private final LifeCounter plugin;

    public Cooldown(LifeCounter plugin) {
        this.plugin = plugin;
    }

    public void setPlayerLiveInCooldown(Player player) {
        after = Instant.now();
        before = after.plus(plugin.getCfg().getInt("REGENERATE-COOLDOWN.cooldown"), ChronoUnit.SECONDS);
        lastLive = plugin.getCounter().getLives(player) + 1;
        plugin.getStorage().getConfiguration().set(player.getUniqueId() + "." + lastLive + ".date", after.toEpochMilli());
        plugin.getStorage().save();
        milli = before.toEpochMilli() - after.toEpochMilli();
    }

    public void setPlayerLiveCanRelive(Player player) {
        plugin.getStorage().getConfiguration().set(player.getUniqueId() + "." + lastLive + ".canRelive", true);
        plugin.getStorage().save();
    }

    public String getDynamicFormattedTime(long time, String daysFormat, String hoursFormat, String minutesFormat, String secondsFormat) {
        long seconds = time / 1000;

        long days = seconds / 86400;
        seconds = seconds - (days * 86400);

        long hours = seconds / 3600;
        seconds = seconds - (hours * 3600);

        long minutes = seconds / 60;
        seconds = seconds - (minutes * 60);

        String format = "";

        if (days > 0) {
            format = format + daysFormat.replaceAll("%days%", String.valueOf(days));
        }

        if (hours > 0) {
            format = format + hoursFormat.replaceAll("%hours%", String.valueOf(hours));
        }

        if (minutes > 0) {
            format = format + minutesFormat.replaceAll("%minutes%", String.valueOf(minutes));
        }

        format = format + secondsFormat.replaceAll("%seconds%", String.valueOf(seconds));

        return format;
    }

    public Instant getAfter() {
        return after;
    }

    public Instant getBefore() {
        return before;
    }

    public long getMilli() {
        return milli;
    }

    public void setAfter(Instant after) {
        this.after = after;
    }

    public void setBefore(Instant before) {
        this.before = before;
    }

    public void setMilli(long milli) {
        this.milli = milli;
    }

    public int getLastLive() {
        return lastLive;
    }
}
