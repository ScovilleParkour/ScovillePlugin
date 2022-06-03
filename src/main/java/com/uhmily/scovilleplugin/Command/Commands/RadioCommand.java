package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.RadioInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RadioCommand extends BaseCommand {

    public RadioCommand() {
        super("radio");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.radio")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (!sp.isMusic()) {
            p.sendMessage(ChatHelper.format("music_off", p));
            return false;
        }

        RadioInventory radioInventory = new RadioInventory(p);
        radioInventory.openInventory();

        return false;
    }

}
