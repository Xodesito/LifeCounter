package com.realxode.lifecounter.counter.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainTab implements TabCompleter {

    private final List<String> firstCommands = new ArrayList<>();

    public MainTab() {
        firstCommands.add("set");
        firstCommands.add("add");
        firstCommands.add("remove");
        firstCommands.add("reload");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        switch (args.length) {
            case 1: {
                String first = args[0];
                if (!first.equals("")) {
                    for (String firstCommand:firstCommands) {
                        if (firstCommand.startsWith(first)) {
                            results.add(firstCommand);
                        }
                    }
                }
            }
            case 2: {
                String subCommand = args[0];
                switch (subCommand.toLowerCase()) {
                    case "set":
                    case "add":
                    case "remove": {
                        String inputName = args[1];
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            String name = player.getName();
                            if (inputName.equals("")) {
                                results.add(name);
                            } else {
                                if (name.startsWith(inputName)) {
                                    results.add(name);
                                }
                            }
                        }
                    }
                }
            }
            case 3: {
                String subCommand = args[0];
                switch (subCommand.toLowerCase()) {
                    case "set":
                    case "add":
                    case "remove": {
                        Player player = Bukkit.getPlayerExact(args[1]);
                        if (player != null) {
                            results.add("<value>");
                        } else {
                            results.add("Invalid player!");
                        }
                    }
                }
            }
        }
        return results;
    }

}