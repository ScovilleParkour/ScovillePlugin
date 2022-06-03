package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.OptionsInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OptionsCommand extends ChildCommand {
    public OptionsCommand() {
        PKCommand.getInstance().registerChild("options", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.options")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        OptionsInventory optionsInventory = new OptionsInventory(p);
        optionsInventory.openInventory();
        return false;

    }
}
