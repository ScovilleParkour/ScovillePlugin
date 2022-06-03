package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TagCommand extends ParentCommand {

    private static TagCommand instance;

    public static TagCommand getInstance() {
        return instance;
    }

    public TagCommand() {
        super("tag");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.tag.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        return false;
    }

}
