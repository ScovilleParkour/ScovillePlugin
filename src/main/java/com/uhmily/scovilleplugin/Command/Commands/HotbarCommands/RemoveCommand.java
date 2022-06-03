package com.uhmily.scovilleplugin.Command.Commands.HotbarCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.HotbarCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveCommand extends ChildCommand {

    public RemoveCommand() {
        HotbarCommand.getInstance().registerChild("remove", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.hotbar.remove")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        if (args.length != 1) {
            p.sendMessage(ChatHelper.format("hotbar_remove_help", p));
            return false;
        }

        Hotbar.getHotbars().removeIf(hotbar -> hotbar.getId().equals(args[0]));

        return false;
    }

}
