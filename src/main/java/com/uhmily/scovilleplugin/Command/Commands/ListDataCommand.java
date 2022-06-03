package com.uhmily.scovilleplugin.Command.Commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uhmily.scovilleplugin.Achievements.AchievementManager;
import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Season.Season;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListDataCommand extends BaseCommand {

    public ListDataCommand() {
        super("listdata");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.listdata")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        p.sendMessage("Players: ");
        for (ScovillePlayer sp : ScovillePlayer.getPlayers()) {
            p.sendMessage(sp.toString());
        }
        p.sendMessage("Course: ");
        for (Course c : new ArrayList<>(Course.getCourses())){
            p.sendMessage(c.toString());
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Course.getCourses()));
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Season.getSeasons()));
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ScovillePlayer.getPlayers()));
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(AchievementManager.getAchievements()));
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Hotbar.getHotbars()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return false;
    }

}
