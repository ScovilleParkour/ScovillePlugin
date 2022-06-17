package com.uhmily.scovilleplugin.Listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MusicListener implements Listener {

    public MusicListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp.getCurrentSong() != null) sp.getCurrentSong().pause();
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent e) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp.getCurrentSong() != null && sp.isMusic()) sp.getCurrentSong().run(e.getPlayer());
    }

    public static void playSongAtLocation(ScovillePlayer sp, Location loc) {
        if (sp.getCurrentSong() != null && sp.getCurrentSong().isRadio()) return;

        String songName = null;
        if (Bukkit.getPlayer(sp.getUuid()) == null)
            return;

        boolean song_transition = false;

        if (loc.getWorld().equals(Bukkit.getWorld("practice")))
            songName = "Practice";
        else {
            RegionManager regionManager = WGBukkit.getPlugin().getRegionManager(loc.getWorld());
            ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
            if (set.getRegions().size() == 0) {
                sp.stopSong();
                return;
            }
            for (ProtectedRegion region : set.getRegions()) {
                String song = region.getFlag(ScovillePlugin.SONG_FLAG);
                if (song == null) continue;
                songName = song;
                if (region.getFlag(ScovillePlugin.SONG_TRANSITION_FLAG) != null) {
                    song_transition = Boolean.TRUE.equals(region.getFlag(ScovillePlugin.SONG_TRANSITION_FLAG));
                }
                if (region instanceof GlobalProtectedRegion) {
                    break;
                }
            }
        }
        if (songName == null) sp.stopSong();
        else {
            Song song = Song.getSong(songName);
            if (song_transition)
                song.setTick(sp.getCurrentSong() == null ? 0 : sp.getCurrentSong().getTick());
            if (sp.getCurrentSong() == null) {
                sp.playSong(song);
            } else if (sp.getCurrentSong().getName().equalsIgnoreCase(song.getName())) return;
            else {
                sp.fadeSong(song);
            }
        }
    }

    @EventHandler
    public void onEnterRegion(PlayerMoveEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (sp.isMusic() && sp.getCurrentSong() != null && sp.getCurrentSong().isRadio()) return;
        if (!sp.isMusic()) return;
        playSongAtLocation(sp, e.getTo());

    }

    @EventHandler
    public void onTPRegion(PlayerTeleportEvent e) {

        ScovillePlayer sp = ScovillePlayer.getPlayer(e.getPlayer());
        if (!sp.isMusic()) return;
        if (sp.isMusic() && sp.getCurrentSong() != null && sp.getCurrentSong().isRadio()) return;
        playSongAtLocation(sp, e.getTo());

    }

}
