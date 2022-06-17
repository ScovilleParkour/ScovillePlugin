package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.HatInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HatsCommand extends BaseCommand {

    public HatsCommand() {
        super("hats");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        HatInventory inv = new HatInventory(p);
        inv.openInventory();

        return false;

    }

}
