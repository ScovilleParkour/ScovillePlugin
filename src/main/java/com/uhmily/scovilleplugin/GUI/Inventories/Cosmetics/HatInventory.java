package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.data.Nametag;
import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HatInventory extends BaseGUI {

    enum HatButton {
        NONE(-1, null, null),
        GREEN(9, new ItemStack(Material.STAINED_GLASS, 1, (byte)13), "green"),
        LIME(10, new ItemStack(Material.STAINED_GLASS, 1, (byte)5), "lime"),
        YELLOW(11, new ItemStack(Material.STAINED_GLASS, 1, (byte)4), "yellow"),
        ORANGE(12, new ItemStack(Material.STAINED_GLASS, 1, (byte)1), "orange"),
        RED(13, new ItemStack(Material.STAINED_GLASS, 1, (byte)14), "red"),
        BLACK(14, new ItemStack(Material.STAINED_GLASS, 1, (byte)15), "black"),
        CACTUS(18, new ItemStack(Material.CACTUS), "cactus"),
        SLIME(19, new ItemStack(Material.SLIME_BLOCK), "slime"),
        WETSPONGE(20, new ItemStack(Material.SPONGE, 1, (byte)1), "wetsponge"),
        JACKOLANTERN(21, new ItemStack(Material.JACK_O_LANTERN), "jackolantern"),
        BIGBRAIN(22, ItemHelper.createItem("", UUID.fromString("e60693ba-e3bb-436a-802d-99dbee5354c7")), "bigbrain"),
        DRAGON(23, new ItemStack(Material.SKULL_ITEM, 1, (byte)5), "dragon"),
        RESET(26, null, ""),
        BACK(31, null, null);

        private final int pos;
        private final ItemStack item;
        private final String ident;

        HatButton(int pos, ItemStack item, String ident) {
            this.pos = pos;
            this.item = item;
            this.ident = ident;
        }

        @NotNull
        public static HatButton forInt(int pos) {
            for (HatButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public ItemStack getItem() {
            return item;
        }

        public String getIdent() {
            return ident;
        }
    }

    public HatInventory(Player p) {
        super(36, ChatHelper.format("hat_inventory_title", p), p);
    }

    private ItemStack createItem(HatButton button) {

        String clickIdent;

        if (this.p.hasPermission("scoville.hat." + button.getIdent())) {
            if (this.p.getInventory().getHelmet() != null && this.p.getInventory().getHelmet().getType().equals(button.getItem().getType()) && this.p.getInventory().getHelmet().getData().getData() == button.getItem().getData().getData())
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("hat_inventory_" + button.getIdent(), this.p),
                button.getItem().clone(),
                ChatHelper.color("&8&m----------------"),
                ChatHelper.format("hat_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                ChatHelper.color("&8&m----------------")
        );
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(0);
        createRow(3);

        this.inv.setItem(HatButton.GREEN.getPos(),        createItem(HatButton.GREEN));
        this.inv.setItem(HatButton.LIME.getPos(),         createItem(HatButton.LIME));
        this.inv.setItem(HatButton.YELLOW.getPos(),       createItem(HatButton.YELLOW));
        this.inv.setItem(HatButton.ORANGE.getPos(),       createItem(HatButton.ORANGE));
        this.inv.setItem(HatButton.RED.getPos(),          createItem(HatButton.RED));
        this.inv.setItem(HatButton.BLACK.getPos(),        createItem(HatButton.BLACK));
        this.inv.setItem(HatButton.CACTUS.getPos(),       createItem(HatButton.CACTUS));
        this.inv.setItem(HatButton.SLIME.getPos(),        createItem(HatButton.SLIME));
        this.inv.setItem(HatButton.WETSPONGE.getPos(),    createItem(HatButton.WETSPONGE));
        this.inv.setItem(HatButton.JACKOLANTERN.getPos(), createItem(HatButton.JACKOLANTERN));
        this.inv.setItem(HatButton.BIGBRAIN.getPos(),     createItem(HatButton.BIGBRAIN));
        this.inv.setItem(HatButton.DRAGON.getPos(),       createItem(HatButton.DRAGON));

        this.inv.setItem(HatButton.RESET.getPos(),        ItemHelper.createItem(ChatHelper.format("hat_inventory_reset", this.p),        Material.BARRIER));

        this.inv.setItem(HatButton.BACK.getPos(),         ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        HatButton button = HatButton.forInt(e.getRawSlot());

        switch (button) {
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                cosmeticInventory.openInventory();
                break;
            case NONE:
                break;
            case RESET:
                this.p.getInventory().setHelmet(null);
                this.updateItems();
                break;
            case BIGBRAIN:
                if (!p.hasPermission("scoville.hat." + button.getIdent())) return;
                this.p.getInventory().setHelmet(ItemHelper.createItem(ChatHelper.format("hat_inventory_" + button.getIdent(), p), this.p));
                this.updateItems();
                break;
            default:
                if (!p.hasPermission("scoville.hat." + button.getIdent())) return;
                this.p.getInventory().setHelmet(ItemHelper.createItem(ChatHelper.format("hat_inventory_" + button.getIdent(), p),  button.getItem().clone()));
                this.updateItems();
                break;
        }

    }

}
