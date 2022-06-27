package com.realxode.lifecounter.counter.events;

import com.realxode.lifecounter.LifeCounter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

import static com.realxode.api.chat.ChatUtil.Chat.centerMessage;
import static com.realxode.api.chat.ChatUtil.Chat.sendCenteredMessageV2;
import static com.realxode.api.chat.ChatUtil.translate;

public class CounterListener implements Listener {
    private final LifeCounter plugin;

    public CounterListener(LifeCounter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!this.plugin.getCounter().isInConfig(player)) {
            this.plugin.getCounter().setLives(player, 3);
            this.plugin.getLogger().log(Level.INFO, plugin.getLang().getString("CONSOLE-LOG-ON-JOIN").replace("{player}", player.getName()));
        }

        if (this.plugin.getCounter().getLives(player) == 0) {
            player.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
        } else {
            (new BukkitRunnable() {
                public void run() {
                    String message = plugin.getLang().getString("ACTION-BAR").replace("{lifes}",
                            String.valueOf(plugin.getCounter().getLives(player)));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
            }).runTaskTimer(plugin, 20L, 20L);
        }
        if (player.getWorld().getGameRuleValue(GameRule.DO_IMMEDIATE_RESPAWN)) return;
        player.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        final Player player = e.getEntity();
        (new BukkitRunnable() {
            public void run() {
                player.spigot().respawn();
            }
        }).runTaskLater(plugin, 2L);
        if (plugin.getCounter().isInConfig(player)) {
            if (plugin.getCounter().getLives(player) - 1 == 0) {
                player.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
                plugin.getCounter().setLives(player, 0);
            } else {
                plugin.getCounter().setLives(player, plugin.getCounter().getLives(player) - 1);
                for (String string : plugin.getLang().getStringList("DEATH-MESSAGE")) {
                    if (string.contains("<#center>")) {
                        sendCenteredMessageV2(player, string
                                .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)))
                                .replace("<#center>", ""));
                    } else {
                        player.sendMessage(translate(string.replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)))));
                    }
                }
                String broadcastMessage = plugin.getLang().getString("BROADCAST-DEATH-MESSAGE")
                        .replace("{player}", player.getName())
                        .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)));
                if (broadcastMessage.contains("<#center>")) {
                    e.setDeathMessage(centerMessage(broadcastMessage
                            .replace("<#center>", "")));
                } else {
                    e.setDeathMessage(broadcastMessage);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        final Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        (new BukkitRunnable() {
            int time = 4;

            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30 * 20, 255));
                --this.time;
                player.sendTitle(plugin.getLang().getString("DEATH-TITLE").replace("{time}", String.valueOf(time))
                        , plugin.getLang().getString("DEATH-SUBTITLE").replace("{time}", String.valueOf(time))
                        , 1, 20, 1);
                if (this.time == 0) {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(player.getBedSpawnLocation());
                    this.cancel();
                }

            }
        }).runTaskTimer(this.plugin, 1L, 20L);
    }
}
