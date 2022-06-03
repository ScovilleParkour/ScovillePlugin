package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import com.uhmily.scovilleplugin.Types.TPSigns.TPSign;
import com.uhmily.scovilleplugin.Types.TagSign.TagSign;
import me.clip.deluxetags.DeluxeTag;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TagSignListener  implements Listener {

    public TagSignListener() {
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    @EventHandler
    public void onTagSignClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getState() instanceof Sign) && !(e.getAction().equals(Action.PHYSICAL) && block.getType().equals(Material.IRON_PLATE))) return;
        if (!TagSign.signExists(block.getLocation())) return;
        TagSign tagSign = TagSign.getSign(block.getLocation());
        Player p = e.getPlayer();
        if (p.hasPermission("deluxetags.tag." + tagSign.getTagID()) && p.isPermissionSet("deluxetags.tag." + tagSign.getTagID())) {
            return;
        }
        User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
        user.data().add(Node.builder("deluxetags.tag." + tagSign.getTagID()).build());
        LuckPermsProvider.get().getUserManager().saveUser(user);
        ChatHelper.broadcastMessage("tag_unlocked", p.getName(), DeluxeTag.getLoadedTag(tagSign.getTagID()).getDisplayTag());
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 100.0F, 100.0F);
    }

}
