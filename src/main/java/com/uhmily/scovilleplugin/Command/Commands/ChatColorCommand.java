package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.ChatColorInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ChatColorCommand extends BaseCommand {

    public ChatColorCommand() {
        super("chatcolor");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        ChatColorInventory inv = new ChatColorInventory(p);
        inv.openInventory();

        return false;

    }

}
