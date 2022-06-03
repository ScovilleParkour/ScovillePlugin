package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends ParentCommand {

    private static LeaderboardCommand instance;

    public static LeaderboardCommand getInstance() {
        return instance;
    }

    public LeaderboardCommand() {
        super("lb");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.leaderboard.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        p.performCommand("lb fastest " + String.join(" ", paramArrayOfString));
        return false;

    }

}
