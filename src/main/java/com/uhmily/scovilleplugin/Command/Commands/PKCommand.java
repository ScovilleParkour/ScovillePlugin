package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PKCommand extends ParentCommand {

    private static PKCommand instance;

    public static PKCommand getInstance() {
        return instance;
    }

    public PKCommand() {
        super("pk");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.pk.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        return false;
    }

}
