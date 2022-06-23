package com.uhmily.scovilleplugin.Command.Commands.LBCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.LeaderboardCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FastestCommand extends ChildCommand {

    public FastestCommand() {
        LeaderboardCommand.getInstance().registerChild("fastest", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.leaderboard.fastest")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        final String[] COLORS = { "&4", "&c", "&6", "&e", "&a", "&2", "&2", "&2", "&2", "&2" };

        if (args.length == 0) {
            p.sendMessage(ChatHelper.format("leaderboard_fastest_help", p));
            return false;
        }

        String courseName = String.join(" ", args);
        Course course = Course.getCourseOrNull(courseName);

        if (course == null || !course.playerHasPermissions(p)) {
            p.sendMessage(ChatHelper.format("course_not_found", p));
            return false;
        }

        ArrayList<BaseComponent> components = new ArrayList<BaseComponent>(){{
            add(new TextComponent(" "));
            add(new TextComponent(ChatHelper.color("&8&m*--&r&8[" + course.getColoredName() + "&8]&8&m--*")));
            add(new TextComponent(" "));
        }};

        ArrayList<Map.Entry<UUID, Long>> times = course.getLeaderboardTimes().entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(10).collect(Collectors.toCollection(ArrayList::new));

        if (times.size() == 0) {
            p.performCommand("lb beaten " + courseName);
            return false;
        }

        for (int i = 0; i < times.size(); i++) {
            Map.Entry<UUID, Long> time = times.get(i);
            components.add(new TextComponent(ChatHelper.color("&8&m--*&r " + COLORS[i] + (i + 1) + ". &7" + UUIDFetcher.getName(time.getKey()) + " &8- " + COLORS[i] + DurationFormatUtils.formatDurationHMS(time.getValue()) + " &8&m*--")));
        }

        components.add(new TextComponent(" "));

        TextComponent textComponent1 = new TextComponent(ChatHelper.color("&8&m&l*--&8&l &a[" + ChatHelper.format("lb_fastest_time", p) + "] "));
        TextComponent rightArrow = new TextComponent(ChatHelper.color("&7[" + ChatHelper.format("lb_times_beaten", p) + "]"));
        rightArrow.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lb beaten " + course.getName()));
        rightArrow.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] { new TextComponent(ChatHelper.format("lb_times_beaten_prompt", p)) }));
        textComponent1.addExtra(rightArrow);
        TextComponent end = new TextComponent(ChatHelper.color(" &8&m&l--*"));
        textComponent1.addExtra(end);
        components.add(textComponent1);
        for (BaseComponent comp : components)
            p.spigot().sendMessage(comp);

        return false;
    }

}
