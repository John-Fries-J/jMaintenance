package org.johnfries.jMaintenence.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.johnfries.jMaintenence.JMaintenence;

public class off implements CommandExecutor {
    private final JMaintenence plugin;
    private final on onCommand;

    public off(JMaintenence plugin, on onCommand) {
        this.plugin = plugin;
        this.onCommand = onCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jmaintenence.whitelist.toggle")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (!onCommand.isWhitelistEnabled()) {
            sender.sendMessage("§cThe Maintenence is already disabled.");
            return true;
        }

        onCommand.setWhitelistEnabled(false);
        sender.sendMessage("§aMaintenence has been disabled.");
        return true;
    }
}
