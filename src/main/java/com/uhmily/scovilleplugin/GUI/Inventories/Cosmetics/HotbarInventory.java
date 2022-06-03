package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HotbarInventory extends BaseGUI {

    enum HotbarButton {
        NONE(-1),
        BACK(45),
        RESET(49),
        NEXT(53);

        private final int pos;

        HotbarButton(int pos) {this.pos = pos;}

        @NotNull
        public static HotbarButton forInt(int pos) {
            for (HotbarButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

    }

    private final ArrayList<Hotbar> bars;

    private int page = 0;

    public HotbarInventory(Player p) {
        super(54, ChatHelper.format("hotbar_inventory_title", p), p);
        this.bars = new ArrayList<>(Hotbar.getHotbars());
    }

    private boolean hasNextPage() {
        return bars.size() > 4*(page+1);
    }

    private boolean hasPrevPage() {
        return page > 0;
    }

    public List<Hotbar> getBarsForPage(int page) {

        if (page * 4 >= this.bars.size()) {
            return new ArrayList<>();
        }

        return this.bars.subList(page * 4, Math.min((page + 1) * 4, this.bars.size()));

    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(4);

        this.inv.setItem(1, ItemHelper.createItem("", Material.STAINED_GLASS_PANE, (byte)15));
        this.inv.setItem(10, ItemHelper.createItem("", Material.STAINED_GLASS_PANE, (byte)15));
        this.inv.setItem(19, ItemHelper.createItem("", Material.STAINED_GLASS_PANE, (byte)15));
        this.inv.setItem(28, ItemHelper.createItem("", Material.STAINED_GLASS_PANE, (byte)15));

        int row = 0;
        for (Hotbar bar : getBarsForPage(this.page)) {
            ItemStack selectionItem = new ItemStack(Material.INK_SACK, 1, this.p.hasPermission("scoville.hotbar." + bar.getId()) ? (Objects.equals(ScovillePlayer.getPlayer(p).getHotBar(), bar.getUuid()) ? (byte)10 : (byte)1) : (byte)8);
            this.inv.setItem(9 * row, ItemHelper.createItem(ChatHelper.color(bar.getName()), selectionItem));
            this.inv.setItem(9 * row + 3, bar.getCPItem().getItem(p, ChatHelper.format("item_checkpoint", p)));
            this.inv.setItem(9 * row + 5, bar.getMenuItem().getItem(p, ChatHelper.format("item_menu", p)));
            this.inv.setItem(9 * row + 7, bar.getOptionsItem().getItem(p, ChatHelper.format("item_options", p)));
            row++;
        }

        if (hasPrevPage()) {
            this.inv.setItem(HotbarButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_previous_page", p), Material.ARROW));
        } else {
            this.inv.setItem(HotbarButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_back_to_menu", p), Material.ARROW));
        }
        this.inv.setItem(HotbarButton.RESET.getPos(), ItemHelper.createItem(ChatHelper.format("hotbar_inventory_reset", p), Material.BARRIER));
        if (hasNextPage()) {
            this.inv.setItem(HotbarButton.NEXT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_next_page", p), Material.ARROW));
        }

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        HotbarButton button = HotbarButton.forInt(e.getRawSlot());
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        switch (button) {
            case BACK:
                if (hasPrevPage()) {
                    page--;
                } else {
                    CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                    cosmeticInventory.openInventory();
                }
                break;
            case RESET:
                sp.setHotBar(null);
                sp.save();
                p.performCommand("gethotbar");
                break;
            case NEXT:
                if (hasNextPage()) page++;
                break;
            default:
                if (e.getRawSlot() % 9 == 0) {
                    int hotBarNum = e.getRawSlot() / 9;
                    Hotbar bar = getBarsForPage(this.page).get(hotBarNum);
                    if (!p.hasPermission("scoville.hotbar." + bar.getId())) return;
                    sp.setHotBar(bar.getUuid());
                    sp.save();
                    p.performCommand("gethotbar");
                    break;
                }
        }

        this.updateItems();

    }

}
