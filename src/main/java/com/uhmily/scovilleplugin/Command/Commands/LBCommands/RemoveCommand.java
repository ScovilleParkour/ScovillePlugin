package com.uhmily.scovilleplugin.Command.Commands.LBCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.HotbarCommand;
import com.uhmily.scovilleplugin.Command.Commands.LeaderboardCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RemoveCommand extends ChildCommand {

    public RemoveCommand() {
        LeaderboardCommand.getInstance().registerChild("remove", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.leaderboard.remove")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        if (args.length < 2) {
            p.sendMessage(ChatHelper.format("leaderboard_remove_help", p));
            return false;
        }

        String username = args[0];
        Course course = Course.getCourse(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

        course.getLeaderboardTimes().remove(UUIDFetcher.getUUID(username));
        course.save();

        return false;
    }

}
