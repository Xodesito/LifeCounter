package com.realxode.lifecounter.counter.command;

import com.realxode.api.chat.ChatUtil.Chat;
import com.realxode.lifecounter.LifeCounter;
import com.realxode.lifecounter.counter.Counter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static com.realxode.api.chat.ChatUtil.translate;

public class MainCommand implements CommandExecutor {
    private final LifeCounter plugin;

    public MainCommand(LifeCounter plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lifecounter.use")) {
            sender.sendMessage(plugin.getLang().getString("NO-PERMS"));
            return true;
        } else if (args.length == 0) {

            for (String string : this.plugin.getHelp().getStringList("HELP-MESSAGE")) {
                if (string.contains("<#center>")) {
                    Chat.sendCenteredMessageV2(sender, translate(string.replace("<#center>", "")));
                } else {
                    sender.sendMessage(translate(string));
                }
            }

            return true;
        } else {
            Player target;
            boolean valueBoolean;
            Counter counter;
            int value;
            switch (args[0].toLowerCase()) {
                case "set":
                    if (args.length == 1) {
                        sender.sendMessage(plugin.getLang().getString("NO-ARGS-SET"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(plugin.getLang().getString("PLAYER-NO-EXISTS"));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(plugin.getLang().getString("SPECIFY-VALUE"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var14) {
                        sender.sendMessage(plugin.getLang().getString("VALUE-ERROR"));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.setLives(target, value);
                    sender.sendMessage(plugin.getLang().getString("LIFES-SET")
                            .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(target)))
                            .replace("{player}", target.getName()));
                    target.sendMessage(plugin.getLang().getString("ADMIN-SET")
                            .replace("{lifes}", String.valueOf(plugin.getCounter().getLives(target))));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
                    }
                    break;
                case "add":
                    if (args.length == 1) {
                        sender.sendMessage(plugin.getLang().getString("NO-ARGS-ADD"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(plugin.getLang().getString("PLAYER-NO-EXISTS"));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(plugin.getLang().getString("SPECIFY-VALUE"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var13) {
                        sender.sendMessage(plugin.getLang().getString("VALUE-ERROR"));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.addLives(target, value);
                    sender.sendMessage(plugin.getLang().getString("LIFES-ADDED")
                            .replace("{lifes}", String.valueOf(value))
                            .replace("{player}", target.getName()));
                    target.sendMessage(plugin.getLang().getString("ADMIN-ADDED")
                            .replace("{lifes}", String.valueOf(value)));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
                    }
                    break;
                case "remove":
                    if (args.length == 1) {
                        sender.sendMessage(plugin.getLang().getString("NO-ARGS-REMOVE"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(plugin.getLang().getString("PLAYER-NO-EXISTS"));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(plugin.getLang().getString("SPECIFY-VALUE"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var12) {
                        sender.sendMessage(plugin.getLang().getString("VALUE-ERROR"));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.addLives(target, value);
                    sender.sendMessage(plugin.getLang().getString("LIFES-REMOVED")
                            .replace("{lifes}", String.valueOf(value))
                            .replace("{player}", target.getName()));
                    target.sendMessage(plugin.getLang().getString("ADMIN-REMOVED")
                            .replace("{lifes}", String.valueOf(value)));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(plugin.getLang().getString("KICK-MESSAGE"));
                    }
                    break;
                case "reload":
                    this.plugin.getStorage().reload();
                    plugin.getCfg().reload();
                    plugin.getLang().reload();
                    this.plugin.getHelp().reload();
                    sender.sendMessage(translate("&eThe plugin has been successfully reloaded!"));
                    plugin.getLogger().log(Level.INFO, "The plugin has been successfully reloaded! You are using version: " + plugin.getVersion());
                    plugin.getLogger().log(Level.INFO, "This plugin was developed by Xodesito. " +
                            "If you need support REGARDING THE PLUGIN, join the Discord and feel free to ask!");
                    plugin.getLogger().log(Level.INFO, "Support Discord: https://discord.gg/BD5TdFsgXa");
                    break;
                default:
                    for (String string : this.plugin.getHelp().getStringList("HELP-MESSAGE")) {
                        if (string.contains("<#center>")) {
                            Chat.sendCenteredMessageV2(sender, translate(string.replace("<#center>", "")));
                        } else {
                            sender.sendMessage(translate(string));
                        }
                    }
            }

            return false;
        }
    }
}
