package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Tag.TagType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class TagMenuInventory extends BaseGUI {

    enum TagMenuButton {
        NONE(-1),
        RANK(10),
        COURSE(13),
        SHINY(16),
        HIDDEN(28),
        RECORD(31),
        SPECIAL(34),
        BACK(36);

        private final int pos;

        TagMenuButton(int pos) { this.pos = pos; }

        @NotNull
        public static TagMenuButton forInt(int pos) {
            for (TagMenuButton buttons : values()) {
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

    public TagMenuInventory(Player p) {
        super(45, ChatHelper.format("tag_menu_inventory_title", p), p);
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createBorder();

        this.inv.setItem(TagMenuButton.RANK.getPos(),    ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_rank_tags", p),   "http://textures.minecraft.net/texture/49958766c85fdf9d41ae847b36bbd4c9c6978b6fac6f446d65d6dbe16622c90"));
        this.inv.setItem(TagMenuButton.COURSE.getPos(),  ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_course_tags", p), "http://textures.minecraft.net/texture/daa8fc8de6417b48d48c80b443cf5326e3d9da4dbe9b25fcd49549d96168fc0"));
        this.inv.setItem(TagMenuButton.SHINY.getPos(),   ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_shiny_tags", p),  "http://textures.minecraft.net/texture/57c66f5a4b408005b336da6676e8f6a2a67eea315fb7e91360acc047802fa320"));
        this.inv.setItem(TagMenuButton.HIDDEN.getPos(),  ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_hidden_tags", p), "http://textures.minecraft.net/texture/89a995928090d842d4afdb2296ffe24f2e944272205ceba848ee4046e01f3168"));
        this.inv.setItem(TagMenuButton.RECORD.getPos(),  ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_record_tags", p), "http://textures.minecraft.net/texture/e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe"));
        this.inv.setItem(TagMenuButton.SPECIAL.getPos(), ItemHelper.createItem(ChatHelper.format("tag_menu_inventory_special_tags", p),"http://textures.minecraft.net/texture/df7467c5f738c641246c09f8ce791e339a86e81de62049b41f492888172fa726"));

        this.inv.setItem(TagMenuButton.BACK.getPos(),    ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p),                Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        TagType type = null;

        switch (TagMenuButton.forInt(e.getRawSlot())) {
            case RANK:
                type = TagType.RANK;
                break;
            case COURSE:
                type = TagType.COURSE;
                break;
            case SHINY:
                type = TagType.SHINY;
                break;
            case HIDDEN:
                type = TagType.HIDDEN;
                break;
            case RECORD:
                type = TagType.RECORD;
                break;
            case SPECIAL:
                type = TagType.SPECIAL;
                break;
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(p);
                cosmeticInventory.openInventory();
                return;
        }

        if (type == null) return;

        TagListInventory tagListInventory = new TagListInventory(p, type);
        tagListInventory.openInventory();

    }

}
