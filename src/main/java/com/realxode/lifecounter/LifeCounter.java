package com.realxode.lifecounter;

import com.realxode.api.file.FileConfig;
import com.realxode.lifecounter.counter.Counter;
import com.realxode.lifecounter.counter.cmds.MainCommand;
import com.realxode.lifecounter.counter.cmds.MainTab;
import com.realxode.lifecounter.counter.events.CounterListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static com.realxode.api.chat.ChatUtil.translate;

public final class LifeCounter extends JavaPlugin {
    private final FileConfig storage = new FileConfig(this, "storage.yml");
    private final FileConfig cfg = new FileConfig(this, "settings.yml");
    private final FileConfig help = new FileConfig(this, "help.yml");
    private final FileConfig lang = new FileConfig(this, "lang.yml");
    private final PluginDescriptionFile pdf = this.getDescription();
    private final String version;
    private Counter counter;

    public LifeCounter() {
        this.version = this.pdf.getVersion();
    }

    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting plugin LifeCounter with version " + this.version + ".");
        new UpdateChecker(this, 102947).getVersion(latestVersion -> {
            if (this.getDescription().getVersion().equals(latestVersion)) {
                getLogger().log(Level.INFO, "There is no update available.");
            } else {
                getLogger().log(Level.WARNING, "There is an update available! It is important to have the plugin updated.");
                getLogger().log(Level.INFO, "Your version: " + getVersion() + " | Latest version: " + latestVersion);
            }
        });
        this.counter = new Counter(this);
        this.getCommand("lifecounter").setExecutor(new MainCommand(this));
        this.getCommand("lifecounter").setTabCompleter(new MainTab());
        Bukkit.getPluginManager().registerEvents(new CounterListener(this), this);
        Bukkit.getConsoleSender().sendMessage(translate("&d[LifeCounter] &fIf you need any kind of support, " +
                "don't hesitate to enter our Support Discord and ask anything! You will be welcome."));
        Bukkit.getConsoleSender().sendMessage(translate("&d[LifeCounter] &fSupport Discord: &b&nhttps://discord.gg/BD5TdFsgXa"));
    }

    public void onDisable() {
    }

    public FileConfig getStorage() {
        return this.storage;
    }

    public FileConfig getHelp() {
        return this.help;
    }

    public FileConfig getLang() {
        return lang;
    }

    public Counter getCounter() {
        return this.counter;
    }

    public String getVersion() {
        return version;
    }

    public FileConfig getCfg() {
        return cfg;
    }
}
