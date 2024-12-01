package org.johnfries.jMaintenence.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.johnfries.jMaintenence.JMaintenence;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class on implements CommandExecutor, Listener {
    private final JMaintenence plugin;
    private boolean whitelistEnabled;
    private FileConfiguration whitelistConfig;
    private File whitelistFile;
    public on(JMaintenence plugin) {
        this.plugin = plugin;
        this.whitelistEnabled = false;
        initializeWhitelist();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        registerCommands();
    }
    private void initializeWhitelist() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        whitelistFile = new File(dataFolder, "whitelist.yml");
        if (!whitelistFile.exists()) {
            try {
                whitelistFile.createNewFile();
                whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);
                whitelistConfig.set("reason", "The server is in maintenence!");
                whitelistConfig.set("whitelisted-players", List.of("John_fries"));
                whitelistConfig.save(whitelistFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create whitelist.yml!");
                e.printStackTrace();
            }
        } else {
            whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);
        }
    }

    private void registerCommands() {
        plugin.getCommand("maintenenceon").setExecutor(this);
        plugin.getCommand("maintenenceoff").setExecutor(new off(plugin, this));
        plugin.getCommand("maintenence").setExecutor(new WhitelistCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jmaintenence.whitelist.toggle")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        whitelistEnabled = !whitelistEnabled;
        plugin.getConfig().set("enabled", whitelistEnabled);
        plugin.saveConfig();
        sender.sendMessage("§aWhitelist has been " + (whitelistEnabled ? "enabled" : "disabled") + ".");
        return true;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!whitelistEnabled) return;
        reloadWhitelist();
        Player player = event.getPlayer();
        List<String> whitelistedPlayers = whitelistConfig.getStringList("whitelisted-players");
        String reason = whitelistConfig.getString("reason", "You are not whitelisted!");
        if (!whitelistedPlayers.contains(player.getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, reason);
        }
    }

    private void reloadWhitelist() {
        whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);
    }
    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }
    public void setWhitelistEnabled(boolean enabled) {
        this.whitelistEnabled = enabled;
    }
}
