package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.JoinLeaveInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class JoinMessagesCommand extends BaseCommand {

    public JoinMessagesCommand() {
        super("joinmessages");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        JoinLeaveInventory inv = new JoinLeaveInventory(p);
        inv.openInventory();

        return false;

    }
}
