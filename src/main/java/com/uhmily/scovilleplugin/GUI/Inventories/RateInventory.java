package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RateInventory extends BaseGUI {

    enum RateButton {
        NONE(-1),
        ZERO(4),
        ONE(9),
        TWO(11),
        THREE(13),
        FOUR(15),
        FIVE(17),
        EXIT(22);

        private final int pos;

        RateButton(int pos) {this.pos = pos;}

        @NotNull
        public static RateButton forInt(int pos) {
            for (RateButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public int getInt() { return this.ordinal() - 1; }

    }

    private final Course course;

    public RateInventory(Player p, UUID courseUUID) {
        super(27, ChatHelper.format("rate_menu_title", p, Course.getCourse(courseUUID).getColoredName()), p);
        this.course = Course.getCourse(courseUUID);
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createBorder();

        this.inv.setItem(RateButton.ZERO.getPos(),  ItemHelper.createItem(ChatHelper.format("rate_menu_zero",  this.p), Material.CONCRETE, (short)7));
        this.inv.setItem(RateButton.ONE.getPos(),   ItemHelper.createItem(ChatHelper.format("rate_menu_one",   this.p), Material.CONCRETE, (short)14));
        this.inv.setItem(RateButton.TWO.getPos(),   ItemHelper.createItem(ChatHelper.format("rate_menu_two",   this.p), Material.CONCRETE, (short)1));
        this.inv.setItem(RateButton.THREE.getPos(), ItemHelper.createItem(ChatHelper.format("rate_menu_three", this.p), Material.CONCRETE, (short)4));
        this.inv.setItem(RateButton.FOUR.getPos(),  ItemHelper.createItem(ChatHelper.format("rate_menu_four",  this.p), Material.CONCRETE, (short)5));
        this.inv.setItem(RateButton.FIVE.getPos(),  ItemHelper.createItem(ChatHelper.format("rate_menu_five",  this.p), Material.CONCRETE, (short)13));
        this.inv.setItem(RateButton.EXIT.getPos(),  ItemHelper.createItem(ChatHelper.format("rate_menu_exit",  this.p), Material.BARRIER));
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;
        switch (RateButton.forInt(e.getRawSlot())) {
            case EXIT:
                break;
            case NONE:
                return;
            default:
                this.course.addRate(RateButton.forInt(e.getRawSlot()).getInt());
                ScovillePlayer sp = ScovillePlayer.getPlayer(e.getWhoClicked().getUniqueId());
                if (sp == null) return;
                sp.addRatedCourse(this.course.getUuid());
                sp.save();
                this.course.save();
        }
        this.p.closeInventory();
    }

}
