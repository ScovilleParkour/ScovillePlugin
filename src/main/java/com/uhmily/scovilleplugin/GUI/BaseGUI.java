package com.uhmily.scovilleplugin.GUI;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class BaseGUI implements Listener {

    protected final Inventory inv;
    protected final Player p;

    public BaseGUI(int size, String title, Player p) {
        this.p = p;
        this.inv = Bukkit.createInventory(null, size, ChatHelper.color(title));
        Bukkit.getPluginManager().registerEvents(this, ScovillePlugin.getInstance());
    }

    /**
     * Opens an inventory for a player.
     */
    public void openInventory() {
        this.initializeItems();
        this.p.openInventory(this.inv);
    }

    /**
     * Updates the currently open inventory.
     */
    protected void updateItems() {
        this.initializeItems();
        this.p.updateInventory();
    }

    /**
     * Initializes the items for a given inventory.
     */
    protected abstract void initializeItems();

    /**
     * Checks if the inventory in an InventoryClickEvent is this inventory.
     *
     * @param e The InventoryClickEvent that was fired
     * @return True if this is the inventory, false if it wasn't.
     */
    protected boolean isThisInv(InventoryClickEvent e) {
        if (!e.getInventory().equals(this.inv)) return false;
        // If the clicked item is in the GUI, stop them from moving the item.
        if (e.getRawSlot() < this.inv.getSize()) e.setResult(Event.Result.DENY);
        return true;
    }

    /**
     * Clears all the items in an inventory.
     */
    protected void clearItems() {
        for (int i = 0; i < this.inv.getSize(); i++)
            this.inv.setItem(i, new ItemStack(Material.AIR));
    }

    /**
     * Creates a glass pane row in an inventory
     *
     * @param row Which row to put the panes on (starts at 0)
     */
    protected void createRow(int row) {
        int[] paneColors = {14, 1, 4, 5, 13};
        int start = 9 * row;
        for (int i = 0; i < 5; i++) {
            this.inv.setItem(start + i, ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
            this.inv.setItem(start + 8 - i, ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
        }
    }

    protected void createBorder() {
        int[] paneColors = {14, 1, 4, 5, 13};
        int lastRow = this.inv.getSize() / 9 - 1;
        createRow(0);
        createRow(lastRow);
        for (int i = 1; i < Math.ceil(this.inv.getSize() / 18.0f); i++) {
            this.inv.setItem(9 * i, ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
            this.inv.setItem(9 * (lastRow - i), ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
            this.inv.setItem(9 * i + 8, ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
            this.inv.setItem(9 * (lastRow - i) + 8, ItemHelper.createItem("&r", Material.STAINED_GLASS_PANE, (short)paneColors[i]));
        }
    }

    @EventHandler
    protected abstract void onInventoryClick(InventoryClickEvent e);

    protected static boolean isNull(ItemStack i) {
        return i == null || i.getType() == Material.AIR;
    }

}