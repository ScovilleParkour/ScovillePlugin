package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MonkeyingAroundAchievement extends Achievement<PlayerMoveEvent> {

    @Override
    @EventHandler
    // Unlock achievement if player enters the "monkey" region
    public void trigger(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        for (final ProtectedRegion r : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())) {
            if (r.getId().equalsIgnoreCase("monkey")) {
                sp.unlockAchievement(this);
            }
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    public String getName() {
        return "achievement_monkeyingaround";
    }

    @Override
    public String getDesc() {
        return "achievement_monkeyingaround_desc";
    }

    @Override
    public String getAchievement() {
        return "easy/monkeying_around";
    }

}
