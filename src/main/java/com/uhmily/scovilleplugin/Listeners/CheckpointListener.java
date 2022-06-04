package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Events.CheckpointSignEvent;
import com.uhmily.scovilleplugin.Events.CourseCompleteEvent;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class CheckpointListener implements Listener {

    public CheckpointListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {

        Block block = e.getClickedBlock();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign)) return;
        Sign sign = (Sign)block.getState();

        if (!(sign.getLine(0).equalsIgnoreCase(ChatHelper.color("&8[&4Scoville&8]")) &&
            sign.getLine(2).equalsIgnoreCase(ChatHelper.color("&2✔ &aCheckpoint &2✔")))) return;

        Player p = e.getPlayer();
        if (!p.isOnGround()) {
            p.sendMessage(ChatHelper.format("checkpoint_not_on_ground", p));
            return;
        }

        Course c = Course.getCourseOrNull(ChatColor.stripColor(sign.getLine(1)));
        if (c == null || !c.playerHasPermissions(p)) return;

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp.isPracticing()) {
            sp.setPracticeCP(p.getLocation());
        } else {
            sp.setCheckpoint(c.getUuid(), p.getLocation());
        }
        p.sendMessage(ChatHelper.format("set_checkpoint", p, c.getColoredName()));

        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 0.5f);

        CheckpointSignEvent checkpointSignEvent = new CheckpointSignEvent(c, p);
        Bukkit.getPluginManager().callEvent(checkpointSignEvent);

    }

}
