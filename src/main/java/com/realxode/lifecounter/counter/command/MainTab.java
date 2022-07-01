package com.realxode.lifecounter.counter.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            results.add("set");
            results.add("add");
            results.add("remove");
            results.add("reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")
                    || args[0].equalsIgnoreCase("add")
                    || args[0].equalsIgnoreCase("remove")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    results.add(player.getName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")
                    || args[0].equalsIgnoreCase("add")
                    || args[0].equalsIgnoreCase("remove")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!args[1].equalsIgnoreCase(player.getName())) {
                        results.add("Invalid player!");
                    } else {
                        results.add("<value>");
                    }
                }
            }
        }

        return results;
    }
}
