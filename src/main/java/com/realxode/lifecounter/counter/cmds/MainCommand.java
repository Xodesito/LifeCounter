package com.realxode.lifecounter.counter.cmds;

import com.realxode.api.chat.ChatUtil.Chat;
import com.realxode.lifecounter.LifeCounter;
import com.realxode.lifecounter.counter.Counter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

import static com.realxode.api.chat.ChatUtil.translate;

public class MainCommand implements CommandExecutor {
    private final LifeCounter plugin;

    public MainCommand(LifeCounter plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lifecounter.use")) {
            sender.sendMessage(translate("&cYou do not have sufficient permissions to use this command!"));
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
                        sender.sendMessage(translate("&cYou must specify a player and a value! Example: /" + label + " set " + sender.getName() + " 3"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(translate("&cThis player does not exist or is not online."));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(translate("&cYou must specify a value!"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var14) {
                        sender.sendMessage(translate("&cThe specified value does not match, verify that it is a valid number."));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.setLives(target, value);
                    sender.sendMessage(translate("&aA value of " + value + " lives has been set to " + target.getName() + "."));
                    target.sendMessage(translate("&6Un administrador te ha establecido el número de vidas en " + value + "."));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(translate("&4&l¡VETADO!\n \n&c¡No te quedan vidas! No puedes entrar nunca más."));
                    }
                    break;
                case "add":
                    if (args.length == 1) {
                        sender.sendMessage(translate("&cYou must specify a player and a value! Example: /" + label + " add " + sender.getName() + " 3"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(translate("&cThis player does not exist or is not online."));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(translate("&cYou must specify a value!"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var13) {
                        sender.sendMessage(translate("&cThe specified value does not match, verify that it is a valid number."));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.addLives(target, value);
                    sender.sendMessage(translate("&aA value of " + value + " lives has been added to " + target.getName() + "."));
                    target.sendMessage(translate("&6Un administrador te ha agregado " + value + " vidas &7(total: " + counter.getLives(target) + ")."));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(translate("&4&l¡VETADO!\n \n&c¡No te quedan vidas! No puedes entrar nunca más."));
                    }
                    break;
                case "remove":
                    if (args.length == 1) {
                        sender.sendMessage(translate("&cYou must specify a player and a value! Example: /" + label + " remove " + sender.getName() + " 3"));
                        return true;
                    }

                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(translate("&cThis player does not exist or is not online."));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(translate("&cYou must specify a value!"));
                        return true;
                    }

                    valueBoolean = true;

                    try {
                        value = Integer.parseInt(args[2]);
                    } catch (NumberFormatException var12) {
                        sender.sendMessage(translate("&cThe specified value does not match, verify that it is a valid number."));
                        break;
                    }

                    counter = new Counter(this.plugin);
                    counter.addLives(target, value);
                    sender.sendMessage(translate("&aA value of " + value + " lives has been removed to " + target.getName() + "."));
                    target.sendMessage(translate("&6Un administrador te ha removido " + value + " vidas."));
                    if (counter.getLives(target.getPlayer()) <= 0) {
                        target.kickPlayer(translate("&4&l¡VETADO!\n \n&c¡No te quedan vidas! No puedes entrar nunca más."));
                    }
                    break;
                case "reload":
                    this.plugin.getCfg().reload();
                    this.plugin.getHelp().reload();
                    sender.sendMessage(translate("&eThe plugin has been successfully reloaded!"));
                    break;
                default:
                    Iterator var10 = this.plugin.getHelp().getStringList("HELP-MESSAGE").iterator();

                    while (var10.hasNext()) {
                        String string = (String) var10.next();
                        Chat.sendCenteredMessageV2(sender, translate(string));
                    }
            }

            return false;
        }
    }
}
