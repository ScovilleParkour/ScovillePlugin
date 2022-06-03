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

public class CommandsCommand extends ChildCommand {

    public CommandsCommand() {
        HelpCommand.getInstance().registerChild("commands", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.help.commands")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        int page = 1;

        if (args.length == 1) page = Integer.parseInt(args[0]);

        TextComponent[] components = Arrays.stream(ChatHelper.formatArray("help_command_commands_" + page, p)).map(TextComponent::new).toArray(TextComponent[]::new);
        components[7].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help commands " + (page == 1 ? 2 : 1)));

        p.spigot().sendMessage(components);

        return false;

    }

}
