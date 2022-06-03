package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HelpCommand extends ParentCommand {

    private static HelpCommand instance;

    public static HelpCommand getInstance() {
        return instance;
    }

    public HelpCommand() {
        super("help");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.help.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        TextComponent message1 = new TextComponent(ChatHelper.format("help_command_1", p));
        TextComponent message2 = new TextComponent(ChatHelper.format("help_command_2", p));
        message2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules"));
        TextComponent message3 = new TextComponent(ChatHelper.format("help_command_3", p));
        message3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help staff"));
        TextComponent message4 = new TextComponent(ChatHelper.format("help_command_4", p));
        message4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help socials"));
        TextComponent message5 = new TextComponent(ChatHelper.format("help_command_5", p));
        message5.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help commands"));
        TextComponent message6 = new TextComponent(ChatHelper.format("help_command_6", p));
        message6.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help info"));
        TextComponent message7 = new TextComponent(ChatHelper.format("help_command_7", p));
        message7.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help staff"));
        TextComponent message8 = new TextComponent(ChatHelper.format("help_command_8", p));
        p.spigot().sendMessage(message1, message2, message3, message4, message5, message6, message7, message8);
        return false;

    }

}
