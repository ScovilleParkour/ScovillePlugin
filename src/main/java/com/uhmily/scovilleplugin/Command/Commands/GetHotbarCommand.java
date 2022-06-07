package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Items.Items.Practice.PracticeFlyItem;
import com.uhmily.scovilleplugin.Items.Items.Practice.SetCpItem;
import com.uhmily.scovilleplugin.Listeners.CheckpointItemListener;
import com.uhmily.scovilleplugin.Listeners.MenuItemListener;
import com.uhmily.scovilleplugin.Listeners.OptionsItemListener;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GetHotbarCommand extends BaseCommand {

    public GetHotbarCommand() {
        super("gethotbar");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.gethotbar")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        CheckpointItemListener.getItem(p);
        MenuItemListener.getItem(p);
        OptionsItemListener.getItem(p);

        ScovillePlayer player = ScovillePlayer.getPlayer(p);
        if (player == null) return false;
        if (!player.isPracticing()) return false;

        ItemStack setCpItem = new SetCpItem().getInstance();
        ItemMeta setCpItemMeta = setCpItem.getItemMeta();
        setCpItemMeta.setDisplayName(ChatHelper.format("set_cp_name", p));
        setCpItem.setItemMeta(setCpItemMeta);
        p.getInventory().setItem(3, setCpItem);
        ItemStack practiceFlyItem = new PracticeFlyItem().getInstance();
        ItemMeta practiceFlyItemMeta = practiceFlyItem.getItemMeta();
        practiceFlyItemMeta.setDisplayName(ChatHelper.format("practice_fly_name", p));
        practiceFlyItem.setItemMeta(practiceFlyItemMeta);
        p.getInventory().setItem(5, practiceFlyItem);

        return false;

    }

}
