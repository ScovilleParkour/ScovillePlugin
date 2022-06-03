package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.MainMenuInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainMenuCommand extends ChildCommand {
    public MainMenuCommand() {
        PKCommand.getInstance().registerChild("menu", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.menu")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        MainMenuInventory mainMenuInventory = new MainMenuInventory(p);
        mainMenuInventory.openInventory();
        return false;

    }
}
