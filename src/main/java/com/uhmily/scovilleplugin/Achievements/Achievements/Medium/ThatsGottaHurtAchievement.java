package com.uhmily.scovilleplugin.Achievements.Achievements.Medium;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class ThatsGottaHurtAchievement extends Achievement<PlayerMoveEvent> {
    @Override
    @EventHandler
    public void trigger(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;
        for (final ProtectedRegion r : WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation())) {
            if (r.getId().equalsIgnoreCase("iro_brown_hole")) {
                sp.unlockAchievement(this);
                Song song = Song.getSong("Brown Hole");
                song.runOnce(p);
            }
        }
    }

    @Override
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.MEDIUM;
    }

    @Override
    public String getName() {
        return "achievement_thatsgottahurt";
    }

    @Override
    public String getDesc() {
        return "achievement_thatsgottahurt_desc";
    }

    @Override
    public String getAchievement() {
        return "medium/thats_gotta_hurt";
    }
}
