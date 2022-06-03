package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class SetAltJoinCommand extends BaseCommand {

    public SetAltJoinCommand() {
        super("altjoin");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.altjoin")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (paramArrayOfString.length == 0) {
            p.sendMessage(ChatHelper.format("altjoin_help", p));
            return false;
        }

        String courseName = String.join(" ", paramArrayOfString);
        RankupCourse course = RankupCourse.getCourseOrNull(courseName);

        if (course == null || !course.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }

        course.setAltJoinLoc(p.getLocation());
        course.save();

        return true;

    }

}
