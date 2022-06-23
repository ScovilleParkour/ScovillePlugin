package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Course.CourseWLR;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class EndPlateListener implements Listener {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public EndPlateListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEndPlatePress(PlayerInteractEvent e) {

        if (!e.getAction().equals(Action.PHYSICAL)) return;
        if (ScovillePlayer.getPlayer(e.getPlayer()).isPracticing()) return;

        Player p = e.getPlayer();

        long COOLDOWN_MILLIS = 5000L;
        if (cooldown.containsKey(p.getUniqueId()) && System.currentTimeMillis() - cooldown.get(p.getUniqueId()) < COOLDOWN_MILLIS) return;

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

        if (ScovillePlayer.getPlayer(p).getTimesCompleted(c.getUuid()) == 0) {
            CourseWLR courseWLR = c.getCourseWLR();
            courseWLR.setClears(c.getCourseWLR().getClears() + 1);
            c.setCourseWLR(courseWLR);
            c.save();
        }

        c.end(p);

        cooldown.put(p.getUniqueId(), System.currentTimeMillis());

    }

}