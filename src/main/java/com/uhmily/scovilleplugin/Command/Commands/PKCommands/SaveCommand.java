package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.JsonHelper;
import com.uhmily.scovilleplugin.Helpers.SaveHelper;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand extends ChildCommand {
    public SaveCommand() {
        PKCommand.getInstance().registerChild("save", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.save")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        JsonHelper.saveData();
        TagManager.save();
        SaveHelper.makeBackup();

        p.sendMessage(ChatHelper.format("pk_saved", p));

        return false;

    }

}
