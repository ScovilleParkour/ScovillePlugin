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

public class SocialsCommand extends ChildCommand {

    public SocialsCommand() {
        HelpCommand.getInstance().registerChild("socials", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.help.socials")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        TextComponent[] components = Arrays.stream(ChatHelper.formatArray("help_command_socials", p)).map(TextComponent::new).toArray(TextComponent[]::new);
        components[1].setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/UbgTYYkR86"));
        components[2].setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://tinyurl.com/486fvbx2"));
        components[3].setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/ScovilleNetwork"));

        p.spigot().sendMessage(components);

        return false;

    }

}
