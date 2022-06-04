package com.uhmily.scovilleplugin.Achievements.Achievements.Medium;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Events.PlayerRankupEvent;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Rank;
import org.bukkit.event.EventHandler;

public class HeatingUpAchievement extends Achievement<PlayerRankupEvent> {
    @Override
    @EventHandler
    public void trigger(PlayerRankupEvent event) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        if (event.getRank().toInt() > Rank.MANZANO.toInt()) {
            sp.unlockAchievement(this);
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.MEDIUM;
    }

    @Override
    public String getName() {
        return "achievement_heatingup";
    }

    @Override
    public String getDesc() {
        return "achievement_heatingup_desc";
    }

    @Override
    public String getAchievement() {
        return "easy/heating_up";
    }
}