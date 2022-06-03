package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Plate.Plate;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Calendar;

public class StartTimeListener implements Listener {

    public StartTimeListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onStartPlatePress(PlayerInteractEvent e) {

        if (!e.getAction().equals(Action.PHYSICAL)) return;
        if (ScovillePlayer.getPlayer(e.getPlayer()).isPracticing()) return;

        Player p = e.getPlayer();
        ScovillePlayer player = ScovillePlayer.getPlayer(p);

        Block plateBlock = e.getClickedBlock();
        Block concrete = e.getClickedBlock().getRelative(BlockFace.DOWN);
        if (plateBlock.getType() != Material.IRON_PLATE || !(concrete.getType() == Material.CONCRETE && concrete.getData() == 5)) {
            return;
        }

        if (!Plate.plateExists(plateBlock.getLocation())) {
            return;
        }

        Plate plate = Plate.getPlate(plateBlock.getLocation());
        Course c = Course.getCourse(plate.getCourseUUID());

        player.addStartTime(c.getUuid(), Calendar.getInstance().getTimeInMillis());
        player.setCheckpoint(c.getUuid(), p.getLocation());
        player.save();

        p.sendMessage(ChatHelper.format("timer_started", p, c.getColoredName()));

    }

}
