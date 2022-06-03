package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.JoinLeaveMessage;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JoinLeaveInventory extends BaseGUI {

    enum JoinLeaveButton {
        NONE(-1, null, null),
        MRCOOL(9, "http://textures.minecraft.net/texture/868f4cef949f32e33ec5ae845f9c56983cbe13375a4dec46e5bbfb7dcb6", "mr_cool"),
        SHAKESPEARE(10, "http://textures.minecraft.net/texture/fb59b146cbe49394dcdff3c150a3b6cd3e7bc493ba8db39c5273d6c7c04a15f1", "shakespeare"),
        SUPERHERO(11, "http://textures.minecraft.net/texture/45435c37edafe165c92ae3736e15c464ad621d61220435d1a39adb4705fb886", "superhero"),
        AVIATION(12, "http://textures.minecraft.net/texture/c779b643831f838fb5efd2bafa4c6ea97bb256839761588525498d6294fc47", "aviation"),
        MONARCH(13, "http://textures.minecraft.net/texture/9ae81d366634833dbbffc28e41eaa0ae77b0ae1ca53cfea9fbe47f74538ac38", "monarch"),
        RESET(17, null, ""),
        BACK(22, null, null);

        private final int pos;
        private final String url, ident;

        JoinLeaveButton(int pos, String url, String ident) {
            this.pos = pos;
            this.url = url;
            this.ident = ident;
        }

        @NotNull
        public static JoinLeaveButton forInt(int pos) {
            for (JoinLeaveButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public String getUrl() {
            return url;
        }

        public String getIdent() {
            return ident;
        }
    }

    public JoinLeaveInventory(Player p) {
        super(27, ChatHelper.format("join_leave_inventory_title", p), p);
    }

    private ItemStack createItem(JoinLeaveButton button) {

        String clickIdent;

        JoinLeaveMessage message = JoinLeaveMessage.fromIdent(button.getIdent());
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        if (this.p.hasPermission("scoville.message." + button.getIdent())) {
            if (Objects.equals(message, sp.getMessage()))
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("join_leave_inventory_" + button.getIdent(), this.p),
                button.getUrl(),
                ChatHelper.color("&8&m----------------"),
                ChatHelper.format("join_leave_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                ChatHelper.color("&8&m----------------")
        );
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(0);
        createRow(2);

        this.inv.setItem(JoinLeaveButton.MRCOOL.getPos(),       createItem(JoinLeaveButton.MRCOOL));
        this.inv.setItem(JoinLeaveButton.SHAKESPEARE.getPos(),  createItem(JoinLeaveButton.SHAKESPEARE));
        this.inv.setItem(JoinLeaveButton.SUPERHERO.getPos(),    createItem(JoinLeaveButton.SUPERHERO));
        this.inv.setItem(JoinLeaveButton.AVIATION.getPos(),     createItem(JoinLeaveButton.AVIATION));
        this.inv.setItem(JoinLeaveButton.MONARCH.getPos(),      createItem(JoinLeaveButton.MONARCH));

        this.inv.setItem(JoinLeaveButton.RESET.getPos(),        ItemHelper.createItem(ChatHelper.format("join_leave_inventory_reset", this.p),        Material.BARRIER));

        this.inv.setItem(JoinLeaveButton.BACK.getPos(),         ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        JoinLeaveButton button = JoinLeaveButton.forInt(e.getRawSlot());
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        switch (button) {
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                cosmeticInventory.openInventory();
                break;
            case NONE:
                break;
            case RESET:
                sp.setMessage(null);
                sp.save();
                this.updateItems();
                break;
            default:
                if (!p.hasPermission("scoville.message." + button.getIdent())) return;
                sp.setMessage(JoinLeaveMessage.fromIdent(button.getIdent()));
                sp.save();
                this.updateItems();
                break;
        }

    }

}
