package com.uhmily.scovilleplugin.Command.Commands.LBCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.HotbarCommand;
import com.uhmily.scovilleplugin.Command.Commands.LeaderboardCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class RemoveCommand extends ChildCommand {

    public RemoveCommand() {
        LeaderboardCommand.getInstance().registerChild("remove", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.leaderboard.remove.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        if (args.length < 2) {
            p.sendMessage(ChatHelper.format("leaderboard_remove_help", p));
            return false;
        }

        System.out.println(args.length);

        String username = args[1];

        switch (args[0]) {
            case "time": {
                if (!p.hasPermission("scoville.leaderboard.remove.time")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length < 3) {
                    p.sendMessage(ChatHelper.format("leaderboard_remove_time_help", p));
                    return false;
                }

                Course course = Course.getCourse(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));

                course.getLeaderboardTimes().remove(UUIDFetcher.getUUID(username));
                course.save();
                p.sendMessage(ChatHelper.format("leaderboard_remove_time_success", p, username, course.getColoredName()));
                break;
            }
            case "beaten": {
                if (!p.hasPermission("scoville.leaderboard.remove.beaten")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length < 4) {
                    p.sendMessage(ChatHelper.format("leaderboard_remove_beaten_help", p));
                    return false;
                }

                int times = Integer.parseInt(args[2]);
                Course course = Course.getCourse(String.join(" ", Arrays.copyOfRange(args, 3, args.length)));

                UUID uuid = UUIDFetcher.getUUID(username);
                if (uuid == null) {
                    p.sendMessage(ChatHelper.format("unknown_player", p));
                    return false;
                }
                ScovillePlayer player = ScovillePlayer.getPlayer(uuid);
                if (player == null) {
                    p.sendMessage(ChatHelper.format("player_never_joined", p));
                    return false;
                }
                player.getTimesCompleted().put(course.getUuid(), Math.max(player.getTimesCompleted(course.getUuid()) - times, 0));
                player.save();
                p.sendMessage(ChatHelper.format("leaderboard_remove_beaten_success", p, username, course.getColoredName(), player.getTimesCompleted(course.getUuid())));
                break;
            }
        }

        return false;
    }

}
