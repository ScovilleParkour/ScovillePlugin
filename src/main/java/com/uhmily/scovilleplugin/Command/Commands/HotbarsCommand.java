package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.HotbarInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HotbarsCommand extends BaseCommand {

    public HotbarsCommand() {
        super("hotbars");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        HotbarInventory inv = new HotbarInventory(p);
        inv.openInventory();

        return false;

    }
}
