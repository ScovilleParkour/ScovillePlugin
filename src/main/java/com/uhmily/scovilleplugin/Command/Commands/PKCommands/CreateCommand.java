package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.CreateCourseInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand extends ChildCommand {

    public CreateCommand() {
        PKCommand.getInstance().registerChild("create", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.create")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format("pk_create_help", p));
            return false;
        }

        String courseName = String.join(" ", args);
        CreateCourseInventory cci = new CreateCourseInventory(p, courseName);
        cci.openInventory();

        return false;
    }
}
