package com.uhmily.scovilleplugin.Achievements.Achievements.Medium;

import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class FirstAchievement extends Achievement<PlayerJoinEvent> {

    private boolean isAvailable = true;

    @Override
    @EventHandler
    public void trigger(PlayerJoinEvent event) {
        if (isAvailable) {
            isAvailable = false;
            ScovillePlayer sp = ScovillePlayer.getPlayer(event.getPlayer());
            if (sp == null) return;
            if (sp.hasAchievement(this)) return;
            if (!isAvailable) return;
            sp.unlockAchievement(this);
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.MEDIUM;
    }

    @Override
    public String getName() {
        return "achievement_first";
    }

    @Override
    public String getDesc() {
        return "achievement_first_desc";
    }

    @Override
    public String getAchievement() {
        return "medium/FIRST";
    }
}
