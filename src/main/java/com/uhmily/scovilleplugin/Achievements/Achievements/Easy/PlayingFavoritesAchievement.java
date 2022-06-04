package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.fasterxml.jackson.annotation.*;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayingFavoritesAchievement extends Achievement<CourseCompleteEvent> {

    private final HashMap<UUID, ArrayList<UUID>> lastCourses = new HashMap<>();

    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;

        Player p = e.getPlayer();
        Course c = e.getCourse();

        ArrayList<UUID> courses = lastCourses.getOrDefault(p.getUniqueId(), new ArrayList<>());
        courses.add(c.getUuid());
        lastCourses.put(p.getUniqueId(), new ArrayList<>(courses.subList(Math.max(0, courses.size()-10), courses.size())));
        courses = lastCourses.get(p.getUniqueId());

        ArrayList<UUID> finalCourses = courses;
        if (courses.size() == 10 && courses.stream().allMatch(course -> finalCourses.get(0).equals(course))) {
            sp.unlockAchievement(this);
        }

        sp.save();
        this.save();

    }

    @Override
    @JsonIgnore
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "achievement_playingfavorites";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_playingfavorites_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "easy/playing_favorites"; }

    @JsonGetter("lastCourses")
    public HashMap<UUID, ArrayList<UUID>> getLastCourses() {
        return lastCourses;
    }

    @JsonSetter("lastCourses")
    public void putLastCourses(HashMap<UUID, ArrayList<UUID>> lastCourses) {
        this.lastCourses.putAll(lastCourses);
    }

    public void setLastCourses(HashMap<UUID, ArrayList<UUID>> lC) {
        if (lC == null) { lC = new HashMap<>(); }
        lastCourses.clear();
        lastCourses.putAll(lC);
    }
}
