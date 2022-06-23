package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics.*;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class CosmeticInventory extends BaseGUI {

    enum CosmeticButton {
        NONE(-1),
        TAG(10),
        CHATCOLOR(12),
        HOTBAR(14),
        PARTICLE(16),
        DISGUISE(28),
        HAT(30),
        JOIN(32),
        WIN(34),
        BACK(36);

        private final int pos;

        CosmeticButton(int pos) { this.pos = pos; }

        @NotNull
        public static CosmeticButton forInt(int pos) {
            for (CosmeticButton buttons : values()) {
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

    public CosmeticInventory(Player p) {
        super(45, ChatHelper.format("cosmetic_inventory_title", p), p);
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createBorder();

        this.inv.setItem(CosmeticButton.TAG.getPos(),       ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_tags", p),         "https://textures.minecraft.net/texture/2ad8a3a3b36add5d9541a8ec970996fbdcdea9414cd754c50e48e5d34f1bf60a"));
        this.inv.setItem(CosmeticButton.CHATCOLOR.getPos(), ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_chatcolors", p),   "https://textures.minecraft.net/texture/17dd34924d2b6a213a5ed46ae5783f95373a9ef5ce5c88f9d736705983b97"));
        this.inv.setItem(CosmeticButton.HOTBAR.getPos(),    ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_hotbars", p),      "https://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622"));
        this.inv.setItem(CosmeticButton.PARTICLE.getPos(),  ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_particles", p),    "https://textures.minecraft.net/texture/e4d8a8d527f65a4f434f894f7ee42eb843015bda7927c63c6ea8a754afe9bb1b"));
        this.inv.setItem(CosmeticButton.DISGUISE.getPos(),  ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_disguises", p),             Material.SKULL_ITEM, (byte)4));
        this.inv.setItem(CosmeticButton.HAT.getPos(),       ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_hats", p),         "https://textures.minecraft.net/texture/d55b1aa95fdb777179a4bb9c92f116d787eddc97b9b8c1666256eedf2d6b35"));
        this.inv.setItem(CosmeticButton.JOIN.getPos(),      ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_joins", p),        "https://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1"));
        this.inv.setItem(CosmeticButton.WIN.getPos(),       ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_win_messages", p), "https://textures.minecraft.net/texture/e3e45831c1ea817f7477bcaebfa3d2ee3a936ee8ea2b8bde29006b7e9bdf58"));

        this.inv.setItem(CosmeticButton.BACK.getPos(),      ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p),                 Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        switch (CosmeticButton.forInt(e.getRawSlot())) {
            case TAG:
                TagMenuInventory tagMenuInventory = new TagMenuInventory(this.p);
                tagMenuInventory.openInventory();
                break;
            case CHATCOLOR:
                ChatColorInventory chatColorInventory = new ChatColorInventory(this.p);
                chatColorInventory.openInventory();
                break;
            case HOTBAR:
                HotbarInventory hotbarInventory = new HotbarInventory(this.p);
                hotbarInventory.openInventory();
                break;
            case PARTICLE:
                ParticleInventory particleInventory = new ParticleInventory(this.p);
                particleInventory.openInventory();
                break;
            case DISGUISE:
                DisguiseInventory disguiseInventory = new DisguiseInventory(this.p);
                disguiseInventory.openInventory();
                break;
            case HAT:
                HatInventory hatInventory = new HatInventory(this.p);
                hatInventory.openInventory();
                break;
            case JOIN:
                JoinLeaveInventory joinLeaveInventory = new JoinLeaveInventory(this.p);
                joinLeaveInventory.openInventory();
                break;
            case WIN:
                WinMessageInventory winMessageInventory = new WinMessageInventory(this.p);
                winMessageInventory.openInventory();
                break;
            case BACK:
                MainMenuInventory mainMenuInventory = new MainMenuInventory(p);
                mainMenuInventory.openInventory();
                break;
            default:
                break;
        }

    }

}
