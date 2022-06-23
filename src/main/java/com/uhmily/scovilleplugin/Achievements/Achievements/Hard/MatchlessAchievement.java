package com.uhmily.scovilleplugin.Achievements.Achievements.Hard;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CheckpointCreamEvent;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Events.CourseJoinEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MatchlessAchievement extends Achievement<CourseCompleteEvent> {

    private final HashMap<UUID, Integer> mutekiCPs = new HashMap<>();

    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("Muteki"))) return;
        if (mutekiCPs.getOrDefault(event.getPlayer().getUniqueId(), 0) <= 250) {
            sp.unlockAchievement(this);
        }
    }

    @EventHandler
    public void onJoin(CourseJoinEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("Muteki"))) return;
        if (!sp.getCheckpoints().containsKey(event.getCourse().getUuid()) || Objects.equals(sp.getCheckpoint(event.getCourse().getUuid()), event.getCourse().getStartLoc())) {
            mutekiCPs.put(event.getPlayer().getUniqueId(), 0);
        }
    }

    @EventHandler
    public void onCheckpoint(CheckpointCreamEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (!event.getCourse().equals(Course.getCourse("Muteki"))) return;
        mutekiCPs.put(event.getPlayer().getUniqueId(), mutekiCPs.getOrDefault(event.getPlayer().getUniqueId(), 0) + 1);
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.HARD;
    }

    @Override
    public String getName() {
        return "achievement_matchless";
    }

    @Override
    public String getDesc() {
        return "achievement_matchless_desc";
    }

    @Override
    public String getAchievement() {
        return "hard/matchless";
    }
}
