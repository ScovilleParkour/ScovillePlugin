package com.uhmily.scovilleplugin.Achievements.Achievements.Medium;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CheckpointSignEvent;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Events.CourseJoinEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AllStarAchievement extends Achievement<CourseCompleteEvent> {

    private final ArrayList<UUID> currentAttempts = new ArrayList<>();

    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("NBA"))) return;
        if (currentAttempts.contains(event.getPlayer().getUniqueId())) {
            sp.unlockAchievement(this);
        }
    }

    @EventHandler
    public void onJoin(CourseJoinEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("NBA"))) return;
        if (!sp.getCheckpoints().containsKey(event.getCourse().getUuid()) || Objects.equals(sp.getCheckpoint(event.getCourse().getUuid()), event.getCourse().getStartLoc())) {
            currentAttempts.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onCheckpoint(CheckpointSignEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("NBA"))) return;
        currentAttempts.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.MEDIUM;
    }

    @Override
    public String getName() {
        return "achievement_allstar";
    }

    @Override
    public String getDesc() {
        return "achievement_allstar_desc";
    }

    @Override
    public String getAchievement() {
        return "medium/all-star";
    }

    @JsonGetter("currentAttempts")
    public ArrayList<UUID> getCurrentAttempts() {
        return currentAttempts;
    }

    @JsonSetter("currentAttempts")
    public void putCurrentAttempts(ArrayList<UUID> currentAttempts) {
        this.currentAttempts.addAll(currentAttempts);
    }

    public void setCurrentAttempts(ArrayList<UUID> cA) {
        if (cA == null) { cA = new ArrayList<>(); }
        this.currentAttempts.clear();
        this.currentAttempts.addAll(cA);
    }

}
