package com.realxode.lifecounter.counter.event;

import com.realxode.lifecounter.LifeCounter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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

import java.time.LocalDateTime;
import java.util.logging.Level;

import static com.realxode.api.chat.ChatUtil.Chat.centerMessage;
import static com.realxode.api.chat.ChatUtil.Chat.sendCenteredMessageV2;
import static com.realxode.api.chat.ChatUtil.translate;

public class CounterListener implements Listener {
    private final LifeCounter plugin;
    LocalDateTime date;

    public CounterListener(LifeCounter plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!this.plugin.getCounter().isInConfig(player)) {
            this.plugin.getCounter().setLives(player, plugin.getCfg().getInt("DEFAULT-LIVES"));
            this.plugin.getLogger().log(Level.INFO, plugin.getLang().getString("CONSOLE-LOG-ON-JOIN").replace("{player}", player.getName()));
        }

        if (plugin.getCfg().getBoolean("CUSTOM-BAN-ON-FINAL-DEATH")) {
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
        }).runTaskLater(plugin, 1L);
        if (plugin.getCounter().isInConfig(player)) {
            String broadcastMessage = plugin.getLang().getString("BROADCAST-DEATH-MESSAGE")
                    .replace("{player}", player.getName())
                    .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player) - 1));
            if (broadcastMessage.contains("<#center>")) {
                e.setDeathMessage(centerMessage(broadcastMessage
                        .replace("<#center>", "")));
            } else {
                e.setDeathMessage(broadcastMessage);
            }
            if (plugin.getCfg().getBoolean("CUSTOM-BAN-ON-FINAL-DEATH")) {
                if (plugin.getCounter().getLives(player) - 1 == 0) {
                    player.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
                    plugin.getCounter().setLives(player, 0);
                    return;
                }
            }
            plugin.getCounter().setLives(player, plugin.getCounter().getLives(player) - 1);
            if (plugin.getCfg().getStringList("ACTIONS-ON-DEATH") != null) {
                for (String command : plugin.getCfg().getStringList("ACTIONS-ON-DEATH")) {
                    runActions(player, command);
                }
            } else {
                return;
            }

            if (plugin.getCfg().getStringList("ACTIONS-ON-FINAL-DEATH") != null) {
                if (plugin.getCounter().getLives(player) == 0) {
                    for (String command : plugin.getCfg().getStringList("ACTIONS-ON-FINAL-DEATH")) {
                        runActions(player, command);
                    }
                }
            }
            for (String string : plugin.getLang().getStringList("DEATH-MESSAGE")) {
                if (string.contains("<#center>")) {
                    sendCenteredMessageV2(player, string
                            .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)))
                            .replace("<#center>", ""));
                } else {
                    player.sendMessage(translate(string.replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)))));
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        final Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        (new BukkitRunnable() {
            int time = (plugin.getCfg().getInt("RESPAWN-TIME") + 1);

            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30 * 20, 255));
                --this.time;
                player.sendTitle(plugin.getLang().getString("DEATH-TITLE").replace("{time}", String.valueOf(time))
                        , plugin.getLang().getString("DEATH-SUBTITLE").replace("{time}", String.valueOf(time))
                        , 1, 20, 1);
                if (this.time == 0) {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    try {
                        player.setGameMode(GameMode.valueOf(plugin.getCfg().getString("RESPAWN-GAMEMODE").toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        player.sendMessage(translate("An error has occurred, contact an administrator."));
                        Bukkit.getConsoleSender().sendMessage(translate("[LifeCounter] Illegal argument, is \"" + plugin.getCfg().getString("RESPAWN-GAMEMODE") + "\" spelled correctly?"));
                        ex.printStackTrace();
                    }
                    player.teleport(player.getBedSpawnLocation());
                    this.cancel();
                }

            }
        }).runTaskTimer(this.plugin, 1L, 20L);
    }


    // METHODS

    private void runActions(Player player, String command) {
        if (command.contains("[PLAYER] ")) {
            player.performCommand(command
                    .replace("[PLAYER] ", "")
                    .replace("{player}", player.getName())
                    .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player))));
        } else if (command.contains("[CONSOLE] ")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("[CONSOLE] ", "")
                    .replace("{player}", player.getName())
                    .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player))));
        } else if (command.contains("[MESSAGE] ")) {
            if (command.contains("<#center>")) {
                sendCenteredMessageV2(player, command
                        .replace("[MESSAGE] ", "")
                        .replace("{player}", player.getName())
                        .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player))));
            } else {
                player.sendMessage(translate(command
                        .replace("[MESSAGE] ", "")
                        .replace("{player}", player.getName())
                        .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(player)))));
            }
        }
    }

}
