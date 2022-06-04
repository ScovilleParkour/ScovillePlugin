package com.uhmily.scovilleplugin.Items.Items;

import com.uhmily.scovilleplugin.Events.CheckpointCreamEvent;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Items.HotbarItem;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class CheckpointItem extends HotbarItem {

    public CheckpointItem(ItemStack i) {
        super(i);
    }

    public static void onClick(Player p) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp.isPracticing()) {
            if (sp.getPracticeCP() == null) {
                p.sendMessage(ChatHelper.format("no_checkpoint", p));
                return;
            }
            p.teleport(sp.getPracticeCP());
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (!effect.getType().equals(PotionEffectType.NIGHT_VISION))
                    p.removePotionEffect(effect.getType());
            }
        } else {
            UUID currentlyPlaying = sp.getCurrentlyPlaying();
            if (currentlyPlaying == null) {
                p.sendMessage(ChatHelper.format("not_playing_course", p));
                return;
            }
            sp.checkpoint(currentlyPlaying);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (!effect.getType().equals(PotionEffectType.NIGHT_VISION))
                    p.removePotionEffect(effect.getType());
            }
            CheckpointCreamEvent checkpointCreamEvent = new CheckpointCreamEvent(Course.getCourse(currentlyPlaying), p);
            Bukkit.getPluginManager().callEvent(checkpointCreamEvent);
        }
    }
}
