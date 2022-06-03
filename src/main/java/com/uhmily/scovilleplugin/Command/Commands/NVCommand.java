package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NVCommand extends BaseCommand {

    public NVCommand() {
        super("nv");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.nv")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        else
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1));

        return false;
    }

}
