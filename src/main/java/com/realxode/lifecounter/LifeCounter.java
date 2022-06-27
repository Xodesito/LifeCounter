
package com.realxode.lifecounter;

import com.realxode.api.file.FileConfig;
import com.realxode.lifecounter.counter.Counter;
import com.realxode.lifecounter.counter.cmds.MainCommand;
import com.realxode.lifecounter.counter.events.CounterListener;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class LifeCounter extends JavaPlugin {
    private final FileConfig cfg = new FileConfig(this, "cfgFile.yml");
    private final FileConfig help = new FileConfig(this, "help.yml");
    private final PluginDescriptionFile pdf = this.getDescription();
    private final String version;
    private Counter counter;

    public LifeCounter() {
        this.version = this.pdf.getVersion();
    }

    public void onEnable() {
        this.getLogger().log(Level.INFO, "Starting plugin LifeCounter with version " + this.version + ".");
        this.counter = new Counter(this);
        this.getCommand("lifecounter").setExecutor(new MainCommand(this));
        Bukkit.getPluginManager().registerEvents(new CounterListener(this), this);
    }

    public void onDisable() {
    }

    public FileConfig getCfg() {
        return this.cfg;
    }

    public FileConfig getHelp() {
        return this.help;
    }

    public Counter getCounter() {
        return this.counter;
    }
}
