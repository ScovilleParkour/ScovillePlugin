package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class DisableCollisionListener implements Listener {

    public DisableCollisionListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Team t;
        Scoreboard sb = p.getScoreboard();
        if (sb.getEntryTeam(p.getName()) != null) {
            sb.getEntryTeam(p.getName()).setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
            return;
        }
        if (sb.getTeam("acollide") != null) {
            t = sb.getTeam("acollide");
        } else {
            t = sb.registerNewTeam("acollide");
        }
        t.addEntry(p.getName());
        t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

}
