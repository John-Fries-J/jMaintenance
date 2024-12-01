package org.johnfries.jMaintenence;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.johnfries.jMaintenence.commands.WhitelistCommand;
import org.johnfries.jMaintenence.commands.on;
import org.johnfries.jMaintenence.commands.off;
public final class JMaintenence extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getLogger().finest("JMaintenence Is now online!");
        on onCommand = new on(this);
        if (this.getCommand("maintenenceon") != null) {
            this.getCommand("maintenenceon").setExecutor(onCommand);
        }
        if (this.getCommand("maintenenceoff") != null) {
            this.getCommand("maintenenceoff").setExecutor(new off(this, onCommand));
        }
        if (this.getCommand("maintenence") != null) {
            this.getCommand("maintenence").setExecutor(new WhitelistCommand(this));
        }
        boolean isEnabled = getConfig().getBoolean("enabled", false);
        onCommand.setWhitelistEnabled(isEnabled);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().warning("JMaintenence Is now offline!");
    }
}
