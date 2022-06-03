package com.uhmily.scovilleplugin.Command.Commands.HelpCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.HelpCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ListCommand extends ChildCommand {

    public ListCommand() {
        HelpCommand.getInstance().registerChild("list", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.help.list")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        p.sendMessage(ChatHelper.formatArray("help_command_staff_list", p));

        return false;

    }

}
