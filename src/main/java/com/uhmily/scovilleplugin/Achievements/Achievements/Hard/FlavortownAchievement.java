package com.uhmily.scovilleplugin.Achievements.Achievements.Hard;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Difficulty;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

import java.util.*;
import java.util.stream.Collectors;

public class FlavortownAchievement extends Achievement<CourseCompleteEvent> {
    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        Set<Difficulty> diffs = new HashSet<>();
        for (Course c : sp.getTimesCompleted().keySet().stream().map(Course::getCourse).collect(Collectors.toCollection(ArrayList::new))) {
            diffs.add(c.getDifficulty());
        }
        if (diffs.size() == 10) {
            sp.unlockAchievement(this);
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.HARD;
    }

    @Override
    public String getName() {
        return "achievement_flavortown";
    }

    @Override
    public String getDesc() {
        return "achievement_flavortown_desc";
    }

    @Override
    public String getAchievement() {
        return "hard/flavortown";
    }
}
