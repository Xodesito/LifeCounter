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
            this.plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " has not been found in storage! He has been registered with a value of 3 lives.");
        }

        if (this.plugin.getCounter().getLives(player) == 0) {
            player.kickPlayer(translate("&4&l¡VETADO!\n \n&c¡No te quedan vidas! No puedes entrar nunca más."));
        } else {
            (new BukkitRunnable() {
                public void run() {
                    String message = translate("&cTe quedan un total de &4&l" + plugin.getCounter().getLives(player) + "&c vidas");
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
                player.kickPlayer(translate("&4&l¡VETADO!\n \n&c¡No te quedan vidas! No puedes entrar nunca más."));
                plugin.getCounter().setLives(player, 0);
            } else {
                plugin.getCounter().setLives(player, plugin.getCounter().getLives(player) - 1);
                player.sendMessage(" ");
                sendCenteredMessageV2(player, translate("&c&l¡HAS MUERTO!"));
                sendCenteredMessageV2(player, translate("&fPerdiste una vida, te quedan solo &7" +
                        plugin.getCounter().getLives(player) + " &fvidas!"));
                player.sendMessage(" ");
                e.setDeathMessage(centerMessage("&c¡&4" + player.getName() + "&c ha muerto! Le quedan &4&l" +
                        plugin.getCounter().getLives(player) + " &cvidas."));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        final Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.sendTitle(translate("&c&l¡HAS MUERTO!"), translate("&cReaparecerás en &43 &csegundos!"), 1, 20, 1);
        (new BukkitRunnable() {
            int time = 3;

            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30 * 20, 255));
                --this.time;
                player.sendTitle(translate("&c&l¡HAS MUERTO!"), translate("&cReaparecerás en &4" + this.time + " &csegundos!")
                        , 1, 20, 1);
                if (this.time == 0) {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(player.getBedSpawnLocation());
                    this.cancel();
                }

            }
        }).runTaskTimer(this.plugin, 20L, 20L);
    }
}
