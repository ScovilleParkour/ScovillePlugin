package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HotbarCommand extends ParentCommand {

    private static HotbarCommand instance;

    public static HotbarCommand getInstance() {
        return instance;
    }

    public HotbarCommand() {
        super("hotbar");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (!p.hasPermission("scoville.hotbar.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        return false;
    }

}
