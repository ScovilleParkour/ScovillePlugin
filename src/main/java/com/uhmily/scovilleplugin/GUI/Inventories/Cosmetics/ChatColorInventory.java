package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChatColorInventory extends BaseGUI {

    enum ChatColorButton {
        NONE(-1, null, null, 0),
        GREEN(9, "dark_green", ChatColor.DARK_GREEN, 2),
        LIME(10, "green", ChatColor.GREEN, 10),
        YELLOW(11, "yellow", ChatColor.YELLOW, 11),
        ORANGE(12, "gold", ChatColor.GOLD, 14),
        LIGHT_RED(13, "red", ChatColor.RED, 13),
        DARK_RED(14, "dark_red", ChatColor.DARK_RED, 1),
        GRAY(15, "dark_gray", ChatColor.DARK_GRAY, 8),
        PINK(16, "light_purple", ChatColor.LIGHT_PURPLE, 9),
        PURPLE(17, "dark_purple", ChatColor.DARK_PURPLE, 5),
        WHITE(22, "white", ChatColor.WHITE, 15),
        BACK(31, null, null, 0);

        private final int pos;
        private final String ident;
        private final ChatColor chatColor;
        private final byte color;

        ChatColorButton(int pos, String ident, ChatColor color, int colorNum) {
            this.pos = pos;
            this.ident = ident;
            this.chatColor = color;
            this.color = (byte)colorNum;
        }

        @NotNull
        public static ChatColorButton forInt(int pos) {
            for (ChatColorButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        public String getIdent() {
            return ident;
        }

        public byte getColor() {
            return color;
        }
    }

    public ChatColorInventory(Player p) {
        super(36, ChatHelper.format("chatcolor_inventory_title", p), p);
    }

    private ItemStack createItem(ChatColorButton button) {
        String clickIdent;

        if (this.p.hasPermission("essentials.chat." + button.getIdent())) {
            if (Objects.equals(ScovillePlayer.getPlayer(this.p).getChatColor(), button.getChatColor()))
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(ChatHelper.format("chatcolor_inventory_" + button.getIdent() + "_title", p),
                Material.INK_SACK,
                button.getColor(),
                "&8&m----------------",
                ChatHelper.format("chatcolor_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                "&8&m----------------");
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(0);
        createRow(3);

        this.inv.setItem(ChatColorButton.GREEN.getPos(),     createItem(ChatColorButton.GREEN));
        this.inv.setItem(ChatColorButton.LIME.getPos(),      createItem(ChatColorButton.LIME));
        this.inv.setItem(ChatColorButton.YELLOW.getPos(),    createItem(ChatColorButton.YELLOW));
        this.inv.setItem(ChatColorButton.ORANGE.getPos(),    createItem(ChatColorButton.ORANGE));
        this.inv.setItem(ChatColorButton.LIGHT_RED.getPos(), createItem(ChatColorButton.LIGHT_RED));
        this.inv.setItem(ChatColorButton.DARK_RED.getPos(),  createItem(ChatColorButton.DARK_RED));
        this.inv.setItem(ChatColorButton.GRAY.getPos(),      createItem(ChatColorButton.GRAY));
        this.inv.setItem(ChatColorButton.PINK.getPos(),      createItem(ChatColorButton.PINK));
        this.inv.setItem(ChatColorButton.PURPLE.getPos(),    createItem(ChatColorButton.PURPLE));
        this.inv.setItem(ChatColorButton.WHITE.getPos(),     createItem(ChatColorButton.WHITE));

        this.inv.setItem(ChatColorButton.BACK.getPos(),      ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        ChatColorButton button = ChatColorButton.forInt(e.getRawSlot());

        switch (button) {
            case GREEN:
            case LIME:
            case YELLOW:
            case ORANGE:
            case LIGHT_RED:
            case DARK_RED:
            case GRAY:
            case PINK:
            case PURPLE:
            case WHITE:
                if (!p.hasPermission("essentials.chat." + button.getIdent())) return;
                ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);
                sp.setChatColor(button.getChatColor());
                sp.save();
                this.updateItems();
                break;
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(p);
                cosmeticInventory.openInventory();
                break;
            default:
                break;
        }

    }

}
