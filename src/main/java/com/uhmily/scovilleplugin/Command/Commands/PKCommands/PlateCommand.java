package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class PlateCommand extends ChildCommand {

    public PlateCommand() {
        PKCommand.getInstance().registerChild("plate", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!p.hasPermission("scoville.pk.plate")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            return false;
        }

        String courseName = String.join(" ", args);
        Course course = Course.getCourseOrNull(courseName);

        if (course == null || !course.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }

        Block block = p.getTargetBlock((Set<Material>)null, 5);

        if (block == null || !block.getType().equals(Material.IRON_PLATE)) {
            return false;
        }

        Plate plate = Plate.getPlate(block.getLocation());
        plate.setCourseUUID(course.getUuid());
        plate.save();

        return false;
    }
}