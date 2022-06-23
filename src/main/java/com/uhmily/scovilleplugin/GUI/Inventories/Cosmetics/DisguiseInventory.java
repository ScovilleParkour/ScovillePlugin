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
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DisguiseInventory extends BaseGUI {

    enum DisguiseButton {
        NONE(-1, null, null),
        COW(9, new MobDisguise(DisguiseType.COW), "cow"),
        PIG(10, new MobDisguise(DisguiseType.PIG), "pig"),
        CREEPER(11, new MobDisguise(DisguiseType.CREEPER), "creeper"),
        SHEEP(12, new MobDisguise(DisguiseType.SHEEP), "sheep"),
        PIGMAN(13, new MobDisguise(DisguiseType.PIG_ZOMBIE), "pig_zombie"),
        PARROT(18, new MobDisguise(DisguiseType.PARROT), "parrot"),
        WITHER(19, new MobDisguise(DisguiseType.WITHER_SKELETON), "wither_skeleton"),
        VEX(20, new MobDisguise(DisguiseType.VEX), "vex"),
        BLAZE(21, new MobDisguise(DisguiseType.BLAZE), "blaze"),
        PLAYER(22, new PlayerDisguise(UUIDFetcher.getName(UUID.fromString("e60693ba-e3bb-436a-802d-99dbee5354c7"))), "player"),
        RESET(26, null, ""),
        BACK(31, null, null);

        private final int pos;
        private final Disguise disguise;
        private final String ident;

        DisguiseButton(int pos, Disguise disguise, String ident) {
            this.pos = pos;
            this.disguise = disguise;
            this.ident = ident;
        }

        @NotNull
        public static DisguiseButton forInt(int pos) {
            for (DisguiseButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public Disguise getDisguise() {
            return disguise;
        }

        public String getIdent() {
            return ident;
        }
    }

    public DisguiseInventory(Player p) {
        super(36, ChatHelper.format("disguise_inventory_title", p), p);
    }

    private ItemStack createItem(String url, DisguiseButton button) {

        String clickIdent;

        if (this.p.hasPermission("libsdisguises.disguise." + button.getIdent())) {
            if (DisguiseAPI.isDisguised(this.p) && Objects.equals(DisguiseAPI.getDisguise(this.p).getType(), button.getDisguise().getType()))
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("disguise_inventory_" + button.getIdent(), this.p),
                url,
                "&8&m----------------",
                ChatHelper.format("disguise_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                "&8&m----------------"
        );
    }

    private ItemStack createItem(UUID uuid, DisguiseButton button) {

        String clickIdent;

        if (this.p.hasPermission("libsdisguises.disguise." + button.getIdent())) {
            if (DisguiseAPI.isDisguised(this.p) && Objects.equals(DisguiseAPI.getDisguise(this.p).getType(), button.getDisguise().getType()))
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("disguise_inventory_" + button.getIdent(), this.p),
                uuid,
                "&8&m----------------",
                ChatHelper.format("disguise_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                "&8&m----------------"
        );
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(0);
        createRow(3);

        this.inv.setItem(DisguiseButton.COW.getPos(),     createItem("http://textures.minecraft.net/texture/6dc85429eb5bc2bea2c5dbec89e528ed3a54d207fb3744680d62f84edc5e941",  DisguiseButton.COW));
        this.inv.setItem(DisguiseButton.PIG.getPos(),     createItem("http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4",    DisguiseButton.PIG));
        this.inv.setItem(DisguiseButton.CREEPER.getPos(), createItem("http://textures.minecraft.net/texture/f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952", DisguiseButton.CREEPER));
        this.inv.setItem(DisguiseButton.SHEEP.getPos(),   createItem("http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70",   DisguiseButton.SHEEP));
        this.inv.setItem(DisguiseButton.PIGMAN.getPos(),  createItem("http://textures.minecraft.net/texture/74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb",  DisguiseButton.PIGMAN));
        this.inv.setItem(DisguiseButton.PARROT.getPos(),  createItem("http://textures.minecraft.net/texture/f0bfa850f5de4b2981cce78f52fc2cc7cd7b5c62caefeddeb9cf311e83d9097",  DisguiseButton.PARROT));
        this.inv.setItem(DisguiseButton.WITHER.getPos(),  createItem("http://textures.minecraft.net/texture/7953b6c68448e7e6b6bf8fb273d7203acd8e1be19e81481ead51f45de59a8",    DisguiseButton.WITHER));
        this.inv.setItem(DisguiseButton.VEX.getPos(),     createItem("http://textures.minecraft.net/texture/5e7330c7d5cd8a0a55ab9e95321535ac7ae30fe837c37ea9e53bea7ba2de86b",  DisguiseButton.VEX));
        this.inv.setItem(DisguiseButton.BLAZE.getPos(),   createItem("http://textures.minecraft.net/texture/b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0",  DisguiseButton.BLAZE));
        this.inv.setItem(DisguiseButton.PLAYER.getPos(),  createItem(UUID.fromString("e60693ba-e3bb-436a-802d-99dbee5354c7"),     DisguiseButton.PLAYER));

        this.inv.setItem(DisguiseButton.RESET.getPos(), ItemHelper.createItem(ChatHelper.format("disguise_inventory_reset", p), Material.BARRIER));

        this.inv.setItem(DisguiseButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        DisguiseButton button = DisguiseButton.forInt(e.getRawSlot());

        switch (button) {
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                cosmeticInventory.openInventory();
                break;
            case NONE:
                break;
            case RESET:
                if (DisguiseAPI.isDisguised(this.p))
                    DisguiseAPI.getDisguise(this.p).stopDisguise();
                this.updateItems();
                break;
            default:
                if (!p.hasPermission("libsdisguises.disguise." + button.getIdent())) return;
                Nametag tag = NametagEdit.getApi().getNametag(this.p);
                Disguise disguise = button.getDisguise().clone();
                disguise.setCustomDisguiseName(true);
                disguise.setMultiName(tag.getPrefix() + this.p.getName() + tag.getSuffix());
                disguise.setEntity(this.p);
                disguise.startDisguise();
                DisguiseAPI.setViewDisguiseToggled(this.p, false);
                this.updateItems();
                break;
        }

    }

}
