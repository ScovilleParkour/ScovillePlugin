package com.uhmily.scovilleplugin.Achievements.Achievements;

import com.fasterxml.jackson.annotation.*;
import com.uhmily.scovilleplugin.Achievements.Achievement;
import com.uhmily.scovilleplugin.Achievements.AchievementDiff;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayingFavoritesAchievement extends Achievement<PlayerInteractEvent> {

    private final HashMap<UUID, ArrayList<UUID>> lastCourses = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    @Override
    public void trigger(PlayerInteractEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());

        if (sp.hasAchievement(this)) return;
        if (!e.getAction().equals(Action.PHYSICAL)) return;

        Player p = e.getPlayer();

        Block plateBlock = e.getClickedBlock();
        Block concrete = e.getClickedBlock().getRelative(BlockFace.DOWN);
        if (plateBlock.getType() != Material.IRON_PLATE || !(concrete.getType() == Material.CONCRETE && concrete.getData() == 14)) {
            return;
        }

        if (!Plate.plateExists(plateBlock.getLocation())) {
            return;
        }

        Plate plate = Plate.getPlate(plateBlock.getLocation());
        Course c = Course.getCourse(plate.getCourseUUID());

        ArrayList<UUID> courses = lastCourses.getOrDefault(p.getUniqueId(), new ArrayList<>());
        courses.add(c.getUuid());
        lastCourses.put(p.getUniqueId(), new ArrayList<>(courses.subList(Math.max(0, courses.size()-10), courses.size())));

        if (courses.size() == 10 && courses.stream().allMatch(course -> courses.get(0).equals(course))) {
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
        return "achievement_playingfavorites";
    }

    @Override
    @JsonIgnore
    public String getSkullURL() {
        return "http://textures.minecraft.net/texture/649e5c6887c7d434b8a20975dedcaf55b6737367fe64b688fed23ed52cdb";
    }

    @Override
    @JsonIgnore
    public String getDesc() {
        return "achievement_playingfavorites_desc";
    }

    @Override
    @JsonIgnore
    public String getAchievement() { return "scoville:easy/playing_favorites"; }

    @JsonGetter("lastCourses")
    public HashMap<UUID, ArrayList<UUID>> getLastCourses() {
        return lastCourses;
    }

    @JsonSetter("lastCourses")
    public void putLastCourses(HashMap<UUID, ArrayList<UUID>> lastCourses) {
        this.lastCourses.putAll(lastCourses);
    }

    public void setLastCourses(HashMap<UUID, ArrayList<UUID>> lC) {
        if (lC == null) { lC = new HashMap<>(); }
        lastCourses.putAll(lC);
    }
}
