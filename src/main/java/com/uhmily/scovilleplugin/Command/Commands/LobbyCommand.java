package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Items.HotbarItem;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LobbyCommand extends BaseCommand {

    public LobbyCommand() {
        super("l");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.lobby")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        Location tpTo = new Location(Bukkit.getWorld("courses_released"), 0.5, 50.0, 0.5, -90, 0);

        if (p.isOp()) tpTo = new Location(Bukkit.getWorld("Courses"), 100000.5, 4.0, 100000.5, -90, 0);
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        sp.setCurrentlyPlaying(null);
        if (sp.isPracticing())
            p.sendMessage(ChatHelper.format("isnt_practicing", p));
        sp.setPracticing(false);
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            if (HotbarItem.isHotbarItem(item)) continue;
            p.getInventory().setItem(i, null);
        }
        for (PotionEffect effect : p.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.NIGHT_VISION))
                continue;
            p.removePotionEffect(effect.getType());
        }
        p.getInventory().clear();
        p.performCommand("gethotbar");
        p.setFlying(false);
        p.setAllowFlight(false);
        sp.setPracticeCP(null);
        sp.setPracticing(false);
        sp.save();
        p.teleport(tpTo);
        return false;

    }

}
