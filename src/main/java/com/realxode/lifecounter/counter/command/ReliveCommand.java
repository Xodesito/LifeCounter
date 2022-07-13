package com.realxode.lifecounter.counter.command;

import com.realxode.lifecounter.LifeCounter;
import com.realxode.lifecounter.counter.utils.Cooldown;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.realxode.api.chat.ChatUtil.translate;

public class ReliveCommand implements CommandExecutor {

    private final LifeCounter plugin;

    public ReliveCommand(LifeCounter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("This command is available only for players!");
            return true;
        }
        Player player = (Player) sender;
        Cooldown cooldown = plugin.getCooldown();
        if (!plugin.getStorage().getBoolean(player.getUniqueId() + "." + cooldown.getLastLive() + ".canRelive")) {
            player.sendMessage(translate(plugin.getLang().getString("RELIVE-MSG-IN-COOLDOWN")));
            return true;
        }
        player.sendMessage(translate(plugin.getLang().getString("RELIVE-MSG-SUCCESSFUL")));
        plugin.getCounter().addLives(player, 1);
        player.setGameMode(GameMode.valueOf(plugin.getCfg().getString("REGENERATE-COOLDOWN.relive-gamemode").toUpperCase()));
        player.teleport(player.getBedSpawnLocation());
        for (String key : plugin.getStorage().getConfiguration().getConfigurationSection(player.getUniqueId().toString()).getKeys(false)) {
            if (!key.equalsIgnoreCase("lives")) {
                plugin.getStorage().getConfiguration().set(player.getUniqueId() + "." + key, null);
                plugin.getStorage().save();
            }
        }
        return false;
    }
}
