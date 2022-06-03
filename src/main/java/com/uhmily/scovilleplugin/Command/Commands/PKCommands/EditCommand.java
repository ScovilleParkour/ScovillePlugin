package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.GUI.Inventories.CourseEditInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Course.Course;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand extends ChildCommand {

    public EditCommand() {
        PKCommand.getInstance().registerChild("edit", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.edit")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatHelper.format("no_course_name", p));
            return false;
        }

        String courseName = String.join(" ", args);
        Course c = Course.getCourseOrNull(courseName);
        if (c == null || !c.playerHasPermissions(p)) {
            sender.sendMessage(ChatHelper.format("course_not_found", p));
            return true;
        }
        CourseEditInventory cei = new CourseEditInventory(p, c);
        cei.openInventory();

        return false;
    }
}
