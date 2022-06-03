package com.uhmily.scovilleplugin.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ChildCommand {

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            Bukkit.getLogger().info(String.format("Command %s not run by player!", cmd.getName()));
            return true;
        }
        return false;
    }

}