package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import com.uhmily.scovilleplugin.Types.Tag.TagType;
import me.clip.deluxetags.DeluxeTag;
import me.clip.deluxetags.DeluxeTags;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TagListInventory extends BaseGUI {

    enum TagListButton {
        NONE(-1),
        BACK(36),
        RESET(40),
        NEXT(44);

        private final int pos;

        TagListButton(int pos) {this.pos = pos;}

        @NotNull
        public static TagListButton forInt(int pos) {
            for (TagListButton buttons : values()) {
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

    private List<DeluxeTag> tags;

    private final TagType type;
    private int page;

    public TagListInventory(Player p, TagType type) {
        super(45, ChatHelper.format("tag_list_inventory_title", p), p);
        this.type = type;
        this.page = 0;
        this.tags = new ArrayList<>();
    }

    private boolean hasNextPage() {
        return tags.size() > 27*(page+1);
    }

    private boolean hasPrevPage() {
        return page > 0;
    }

    public List<DeluxeTag> getTagsForPage(TagType type, int page) {

        this.tags.clear();
        List<String> tagStrings = TagManager.getTags(type);
        for (String tagIdentifier : tagStrings) {
            DeluxeTag tag = DeluxeTag.getLoadedTag(tagIdentifier);
            if (tag != null) this.tags.add(tag);
        }

        return new ArrayList<>(tags).subList(page * 27, Math.min((page + 1) * 27, tags.size()));

    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(3);

        List<DeluxeTag> initializeTags = getTagsForPage(this.type, this.page);

        for (int i = 0; i < initializeTags.size(); i++) {
            DeluxeTag tag = initializeTags.get(i);
            if (tag.hasTagPermission(this.p)) {
                if (DeluxeTag.hasTagLoaded(this.p) && DeluxeTag.getPlayerTagIdentifier(this.p).equalsIgnoreCase(tag.getIdentifier()))
                    this.inv.setItem(i, ItemHelper.createItem(tag.getDisplayTag(), Material.NAME_TAG, "", tag.getDescription(), "", ChatHelper.format("tag_list_inventory_selected", this.p)));
                else
                    this.inv.setItem(i, ItemHelper.createItem(tag.getDisplayTag(), Material.NAME_TAG, "", tag.getDescription(), "", ChatHelper.format("tag_list_inventory_not_selected", this.p)));
            } else {
                this.inv.setItem(i, ItemHelper.createItem(tag.getDisplayTag(), Material.INK_SACK, (byte)8, "", tag.getDescription(), "", ChatHelper.format("tag_list_inventory_not_unlocked", this.p)));
            }
        }

        if (hasPrevPage()) {
            this.inv.setItem(TagListButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_prev_page", p), Material.ARROW));
        } else {
            this.inv.setItem(TagListButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_back_to_menu", p), Material.ARROW));
        }
        this.inv.setItem(TagListButton.RESET.getPos(), ItemHelper.createItem(ChatHelper.format("tag_list_inventory_reset", p), Material.BARRIER));
        if (hasNextPage()) {
            this.inv.setItem(TagListButton.NEXT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_next_page", p), Material.ARROW));
        }

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        if (e.getRawSlot() < 27) {
            List<DeluxeTag> t = tags.subList(page * 27, page * 27 + Math.min(27, tags.size() - page * 27));
            if (e.getRawSlot() >= t.size()) return;
            DeluxeTag tag = t.get(e.getRawSlot());
            if (!tag.hasTagPermission(p)) return;
            tag.setPlayerTag(this.p);
            sp.setTagIdentifier(tag.getIdentifier());
            sp.save();
            openInventory();
        } else {
            switch (TagListButton.forInt(e.getRawSlot())) {
                case BACK:
                    if (this.page > 0) {
                        page--;
                        openInventory();
                    } else {
                        TagMenuInventory tmi = new TagMenuInventory(p);
                        tmi.openInventory();
                    }
                    return;
                case RESET:
                    DeluxeTags.getPlugin(DeluxeTags.class).getDummy().setPlayerTag(this.p);
                    DeluxeTags.getPlugin(DeluxeTags.class).removeSavedTag(this.p.getUniqueId().toString());
                    sp.setTagIdentifier(null);
                    openInventory();
                    break;
                case NEXT:
                    if (hasNextPage()) {
                        page++;
                        openInventory();
                    }
                    break;
            }
        }

    }

}
