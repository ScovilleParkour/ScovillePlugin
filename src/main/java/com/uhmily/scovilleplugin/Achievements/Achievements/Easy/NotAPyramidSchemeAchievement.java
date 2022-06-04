package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Calendar;
import java.util.UUID;

public class NotAPyramidSchemeAchievement extends Achievement<CourseCompleteEvent> {

    @EventHandler
    @Override
    public void trigger(CourseCompleteEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        if (!event.getCourse().equals(Course.getCourseOrNull("Melonaire"))) return;
        if (sp.hasAchievement(this)) return;

        if (!event.getTotalTime().isPresent()) return;
        long totalTime = event.getTotalTime().get();

        if (totalTime >= 3600000L) {
            sp.unlockAchievement(this);
        }

        sp.save();
        this.save();

    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    public String getName() {
        return "achievement_notapyramidscheme";
    }

    @Override
    public String getDesc() {
        return "achievement_notapyramidscheme_desc";
    }

    @Override
    public String getAchievement() {
        return "easy/not_a_pyramid_scheme";
    }

}
