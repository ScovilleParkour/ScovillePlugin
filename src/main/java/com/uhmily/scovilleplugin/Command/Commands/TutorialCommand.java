package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TutorialCommand extends BaseCommand {

    public TutorialCommand() {
        super("tutorial");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (!p.hasPermission("scoville.tutorial")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        p.teleport(new Location(Bukkit.getWorld("courses_released"), -19999.5, 50.0, 0.5, -90.0f, 0.0f));
        return false;
    }

}
