package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PublicPolicyInfluencerAchievement extends Achievement<PlayerMoveEvent> {
    @Override
    @EventHandler
    public void trigger(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        for (final ProtectedRegion r : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())) {
            if (r.getId().equalsIgnoreCase("spawn_hidden_room")) {
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
        return "achievement_publicpolicyinfluencer";
    }

    @Override
    public String getDesc() {
        return "achievement_publicpolicyinfluencer_desc";
    }

    @Override
    public String getAchievement() {
        return "easy/public_policy_influencer";
    }
}
