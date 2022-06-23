package com.uhmily.scovilleplugin.Listeners;

import com.uhmily.scovilleplugin.Items.HotbarItem;
import com.uhmily.scovilleplugin.ScovillePlugin;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemListener implements Listener {

    public ItemListener() { Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance()); }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
        if (!e.getHand().equals(EquipmentSlot.HAND)) return;

        Player p = e.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if (heldItem == null || heldItem.getType() == Material.AIR) return;

        NBTItem item = new NBTItem(heldItem);
        if (!item.hasKey("HotbarItem")) return;

        try {
            Class<?> clazz = Class.forName(item.getString("HotbarItem"));
            clazz.getDeclaredMethod("onClick", Player.class).invoke(null, p);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

    }

    @EventHandler
    public void OnPlace(BlockPlaceEvent e) {

        ItemStack heldItem = e.getItemInHand();
        if (heldItem == null || heldItem.getType() == Material.AIR) return;

        NBTItem item = new NBTItem(heldItem);
        if (!item.hasKey("HotbarItem")) return;

        e.setCancelled(true);

    }

    @EventHandler
    public void onToss(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (item == null || item.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasKey("HotbarItem")) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(InventoryClickEvent e) {
        if (e.getClick() == ClickType.CREATIVE) return;
        ArrayList<ItemStack> items = new ArrayList<>();
        if (e.getClick() == ClickType.NUMBER_KEY) {
            items.add(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()));
        }
        items.add(e.getCurrentItem());

        items = items.stream().filter(Objects::nonNull).filter(itemStack -> !itemStack.getType().equals(Material.AIR)).collect(Collectors.toCollection(ArrayList::new));

        if (items.size() == 0) return;
        if (items.stream().noneMatch(itemStack -> new NBTItem(itemStack).hasKey("HotbarItem") || new NBTItem(itemStack).hasKey("CanPlaceOn"))) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        for (ItemStack item : e.getInventorySlots().stream().map(rawSlot -> e.getInventory().getItem(rawSlot)).collect(Collectors.toCollection(ArrayList::new))) {
            if (item == null || item.getType() == Material.AIR) continue;

            if (!HotbarItem.isHotbarItem(item)) return;

            e.setCancelled(true);
            break;
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        ItemStack item = e.getOffHandItem();
        if (item == null || item.getType() == Material.AIR) return;

        if (!HotbarItem.isHotbarItem(item)) return;

        e.setCancelled(true);
    }

}
