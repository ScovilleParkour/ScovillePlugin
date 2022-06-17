package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.ParticleInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TrailCommand extends BaseCommand {

    public TrailCommand() {
        super("trail");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        ParticleInventory inv = new ParticleInventory(p);
        inv.openInventory();

        return false;
    }

}
