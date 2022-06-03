package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.ParentCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PracticeCommand extends ParentCommand {

    private static PracticeCommand instance;

    public static PracticeCommand getInstance() {
        return instance;
    }

    public PracticeCommand() {
        super("practice");
        instance = this;
    }

    public static boolean realCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.practice.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return false;

        if (!p.getGameMode().equals(GameMode.CREATIVE) && sp.getCurrentlyPlaying() == null) {
            return false;
        }

        sp.togglePracticing();
        if (sp.isPracticing()) {
            p.sendMessage(ChatHelper.format("is_practicing", p));
            sp.setPracticeStart(p.getLocation());
            if (!p.getGameMode().equals(GameMode.CREATIVE) && sp.getCurrentlyPlaying() != null) {
                Location currLoc = p.getLocation();
                p.teleport(new Location(Bukkit.getWorld("practice"), currLoc.getX(), currLoc.getY(), currLoc.getZ(), currLoc.getYaw(), currLoc.getPitch()));
            }
            sp.setPracticeCP(p.getLocation());
        } else {
            p.sendMessage(ChatHelper.format("isnt_practicing", p));
            p.teleport(sp.getPracticeStart());
            p.setFlying(false);
            p.setAllowFlight(false);
            sp.setPracticeCP(null);
        }
        sp.save();

        return false;

    }

}
