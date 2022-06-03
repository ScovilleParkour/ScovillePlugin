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

public class StaffCommand extends ChildCommand {

    public StaffCommand() {
        HelpCommand.getInstance().registerChild("staff", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.help.staff")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        TextComponent[] components = Arrays.stream(ChatHelper.formatArray("help_command_staff", p)).map(TextComponent::new).toArray(TextComponent[]::new);
        components[1].setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/helpop "));
        components[2].setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/KMjNCHExYC"));

        p.spigot().sendMessage(components);

        return false;

    }

}
