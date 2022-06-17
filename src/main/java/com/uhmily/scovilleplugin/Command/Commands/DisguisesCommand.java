package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.DisguiseInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class DisguisesCommand extends BaseCommand {

    public DisguisesCommand() {
        super("disguises");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        DisguiseInventory inv = new DisguiseInventory(p);
        inv.openInventory();

        return false;

    }

}
