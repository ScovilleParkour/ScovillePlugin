package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Listeners.CheckpointItemListener;
import com.uhmily.scovilleplugin.Listeners.MenuItemListener;
import com.uhmily.scovilleplugin.Listeners.OptionsItemListener;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class GetHotbarCommand extends BaseCommand {

    public GetHotbarCommand() {
        super("gethotbar");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.gethotbar")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        CheckpointItemListener.getItem(p);
        MenuItemListener.getItem(p);
        OptionsItemListener.getItem(p);

        return false;

    }

}
