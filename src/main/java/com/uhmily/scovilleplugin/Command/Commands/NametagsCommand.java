package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.TagMenuInventory;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class NametagsCommand extends BaseCommand {

    public NametagsCommand() {
        super("nametags");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        TagMenuInventory inv = new TagMenuInventory(p);
        inv.openInventory();

        return false;

    }

}
