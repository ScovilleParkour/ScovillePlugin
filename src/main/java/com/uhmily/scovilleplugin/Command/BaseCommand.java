package com.uhmily.scovilleplugin.Command;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor {

    public BaseCommand(String command) {
        ScovillePlugin.getInstance().getCommand(command).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(String.format("Command %s not run by player!", cmd.getName()));
            return true;
        }
        return safeCommand((Player)sender, cmd, label, args);
    }

    public abstract boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString);

}
