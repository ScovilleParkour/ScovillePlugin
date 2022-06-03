package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Items.HotbarItem;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Course.CourseWLR;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class JoinCommand extends BaseCommand {

    public JoinCommand() {
        super("join");
    }

    private String formatCourses(Player p) {
        String beginning = ChatHelper.format("join_list", p);
        ArrayList<String> courseList = new ArrayList<>(Course.getCourses()).stream().filter(course -> course.playerHasPermissions(p)).map(course -> "&7" + course.getName()).collect(Collectors.toCollection(ArrayList::new));
        return ChatHelper.color(beginning + String.join("&c, ", courseList));
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.join")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);

        if (sp.isPracticing()) return false;

        if (paramArrayOfString.length == 0) {
            p.sendMessage(formatCourses(p));
            return false;
        }

        String courseName = String.join(" ", paramArrayOfString);
        Course course = Course.getCourseOrNull(courseName);

        if (course == null || !course.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }

        course.join(p);

        return false;
    }

}
