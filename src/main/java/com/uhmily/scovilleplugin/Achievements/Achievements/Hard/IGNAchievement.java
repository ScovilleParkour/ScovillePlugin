package com.uhmily.scovilleplugin.Achievements.Achievements.Hard;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Difficulty;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;

public class IGNAchievement extends Achievement<CourseCompleteEvent> {

    @Override
    @EventHandler
    public void trigger(CourseCompleteEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (event.getCourse().getDifficulty().equals(Difficulty.BLAZING)) sp.unlockAchievement(this);
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.HARD;
    }

    @Override
    public String getName() {
        return "achievement_ign";
    }

    @Override
    public String getDesc() {
        return "achievement_ign_desc";
    }

    @Override
    public String getAchievement() {
        return "hard/IGN";
    }

}
