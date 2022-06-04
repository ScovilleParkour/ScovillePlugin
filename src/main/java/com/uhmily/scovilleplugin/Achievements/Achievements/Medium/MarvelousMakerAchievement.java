package com.uhmily.scovilleplugin.Achievements.Achievements.Medium;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class MarvelousMakerAchievement extends Achievement<CourseCompleteEvent> {

    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        HashMap<UUID, Integer> authors = new HashMap<>();
        for (Course c : sp.getTimesCompleted().keySet().stream().map(Course::getCourse).collect(Collectors.toCollection(ArrayList::new))) {
            UUID author = c.getAuthorUUID();
            authors.put(author, authors.getOrDefault(author, 0) + 1);
            if (authors.get(author) >= 5) {
                sp.unlockAchievement(this);
                return;
            }
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.MEDIUM;
    }

    @Override
    public String getName() {
        return "achievement_marvelousmaker";
    }

    @Override
    public String getDesc() {
        return "achievement_marvelousmaker_desc";
    }

    @Override
    public String getAchievement() {
        return "medium/marvelous_maker";
    }

}
