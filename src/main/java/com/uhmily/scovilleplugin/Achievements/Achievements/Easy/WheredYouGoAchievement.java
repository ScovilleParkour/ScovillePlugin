package com.uhmily.scovilleplugin.Achievements.Achievements.Easy;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class WheredYouGoAchievement extends Achievement<AfkStatusChangeEvent> {

    private final HashMap<UUID, Long> timeAfk = new HashMap<>();
    private final HashMap<UUID, Long> timeStartedAfk = new HashMap<>();

    @Override
    @EventHandler
    public void trigger(AfkStatusChangeEvent event) {

        UUID pUUID = event.getAffected().getUUID();
        ScovillePlayer sp = ScovillePlayer.getPlayer(pUUID);
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;

        if (event.getValue()) {
            timeStartedAfk.put(pUUID, System.currentTimeMillis());
        } else {
            if (!sp.isOnline()) return;
            long totalTime = timeAfk.getOrDefault(pUUID, 0L);
            totalTime += System.currentTimeMillis() - timeStartedAfk.getOrDefault(pUUID, 0L);
            timeAfk.put(pUUID, totalTime);
            timeStartedAfk.remove(pUUID);
            if (totalTime >= 36000000L) {
                sp.unlockAchievement(this);
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        UUID pUUID = p.getUniqueId();
        if (sp == null) return;

        if (sp.hasAchievement(this)) return;

        if (timeStartedAfk.containsKey(pUUID)) {
            long totalTime = timeAfk.getOrDefault(pUUID, 0L);
            totalTime += System.currentTimeMillis() - timeStartedAfk.getOrDefault(pUUID, 0L);
            timeAfk.put(pUUID, totalTime);
            timeStartedAfk.remove(pUUID);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        UUID pUUID = p.getUniqueId();
        if (sp == null) return;
        if (sp.hasAchievement(this)) return;

        if (timeAfk.getOrDefault(pUUID, 0L) >= 1000L) {
            sp.unlockAchievement(this);
        }
    }

    @Override
    @JsonIgnore
    public AchievementDiff getAchievementDiff() {
        return AchievementDiff.EASY;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "achievement_wheredyougo";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_wheredyougo_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "easy/whered_you_go"; }

    @JsonGetter("timeAfk")
    public HashMap<UUID, Long> getTimeAfk() {
        return timeAfk;
    }

    public void putTimeAfk(HashMap<UUID, Long> tA) {
        if (tA == null) { tA = new HashMap<>(); }
        this.timeAfk.putAll(tA);
    }

    @JsonSetter("timeAfk")
    public void setTimeAfk(HashMap<UUID, Long> tA) {
        if (tA == null) { tA = new HashMap<>(); }
        this.timeAfk.clear();
        this.timeAfk.putAll(tA);
    }

    @JsonGetter("timeStartedAfk")
    public HashMap<UUID, Long> getTimeStartedAfk() {
        return timeStartedAfk;
    }

    public void putTimeStartedAfk(HashMap<UUID, Long> tA) {
        if (tA == null) { tA = new HashMap<>(); }
        this.timeStartedAfk.putAll(tA);
    }

    @JsonSetter("timeStartedAfk")
    public void setTimeStartedAfk(HashMap<UUID, Long> tA) {
        if (tA == null) { tA = new HashMap<>(); }
        this.timeStartedAfk.clear();
        this.timeStartedAfk.putAll(tA);
    }

}
