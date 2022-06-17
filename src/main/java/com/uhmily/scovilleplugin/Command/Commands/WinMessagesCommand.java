package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.WinMessageInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class WinMessagesCommand extends BaseCommand {

    public WinMessagesCommand() {
        super("winmessages");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        WinMessageInventory inv = new WinMessageInventory(p);
        inv.openInventory();

        return false;

    }

}
