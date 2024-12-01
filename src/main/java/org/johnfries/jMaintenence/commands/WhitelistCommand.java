package org.johnfries.jMaintenence.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.johnfries.jMaintenence.JMaintenence;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WhitelistCommand implements CommandExecutor {
    private final JMaintenence plugin;

    public WhitelistCommand(JMaintenence plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jmaintenence.whitelist.edit")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /maintenence <add|remove> <username>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String username = args[1];

        switch (subCommand) {
            case "add":
                return addPlayerToWhitelist(sender, username);
            case "remove":
                return removePlayerFromWhitelist(sender, username);
            default:
                sender.sendMessage("§cInvalid subcommand. Usage: /maintenence <add|remove> <username>");
                return true;
        }
    }

    private boolean addPlayerToWhitelist(CommandSender sender, String username) {
        File whitelistFile = new File(plugin.getDataFolder(), "whitelist.yml");
        FileConfiguration whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);

        List<String> whitelistedPlayers = whitelistConfig.getStringList("whitelisted-players");
        if (whitelistedPlayers.contains(username)) {
            sender.sendMessage("§cPlayer " + username + " is already whitelisted.");
            return true;
        }

        whitelistedPlayers.add(username);
        whitelistConfig.set("whitelisted-players", whitelistedPlayers);

        try {
            whitelistConfig.save(whitelistFile);
            sender.sendMessage("§aPlayer " + username + " has been added to the whitelist.");
        } catch (IOException e) {
            sender.sendMessage("§cAn error occurred while saving the whitelist.");
            e.printStackTrace();
        }

        return true;
    }

    private boolean removePlayerFromWhitelist(CommandSender sender, String username) {
        File whitelistFile = new File(plugin.getDataFolder(), "whitelist.yml");
        FileConfiguration whitelistConfig = YamlConfiguration.loadConfiguration(whitelistFile);

        List<String> whitelistedPlayers = whitelistConfig.getStringList("whitelisted-players");
        if (!whitelistedPlayers.contains(username)) {
            sender.sendMessage("§cPlayer " + username + " is not on the whitelist.");
            return true;
        }

        whitelistedPlayers.remove(username);
        whitelistConfig.set("whitelisted-players", whitelistedPlayers);

        try {
            whitelistConfig.save(whitelistFile);
            sender.sendMessage("§aPlayer " + username + " has been removed from the whitelist.");
        } catch (IOException e) {
            sender.sendMessage("§cAn error occurred while saving the whitelist.");
            e.printStackTrace();
        }

        return true;
    }
}
