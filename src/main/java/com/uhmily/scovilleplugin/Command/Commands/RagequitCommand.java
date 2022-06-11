package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class RagequitCommand extends BaseCommand {

    public RagequitCommand() {
        super("ragequit");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (!p.hasPermission("scoville.ragequit.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        p.kickPlayer("RAGEQUIT!!!");
        ChatHelper.broadcastMessage("ragequit_message", p.getName());
        return false;
    }

}
