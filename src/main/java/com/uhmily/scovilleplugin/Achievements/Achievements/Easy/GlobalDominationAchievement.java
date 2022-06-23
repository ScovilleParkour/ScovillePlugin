package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

public class GlobalDominationAchievement extends Achievement<CourseCompleteEvent> {

    @EventHandler
    @Override
    public void trigger(CourseCompleteEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;

        if (sp.getTimesCompleted(Course.getCourse("World of A").getUuid()) >= 1 && sp.getTimesCompleted(Course.getCourse("World of B").getUuid()) >= 1 && sp.getTimesCompleted(Course.getCourse("World of C").getUuid()) >= 1 && sp.getTimesCompleted(Course.getCourse("World of D").getUuid()) >= 1) {
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
        return "achievement_globaldomination";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_globaldomination_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "easy/global_domination"; }

}
