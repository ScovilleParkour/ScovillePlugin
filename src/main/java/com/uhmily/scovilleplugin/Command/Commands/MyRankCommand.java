package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class MyRankCommand extends BaseCommand {

    public MyRankCommand() {
        super("myrank");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.myrank")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        RankupCourse course = RankupCourse.getCourseOrNull(sp.getRank());

        if (course == null || !course.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }

        course.join(p);

        return false;
    }

}
