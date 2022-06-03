package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import me.kvq.plugin.trails.API.SuperTrailsAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ParticleInventory extends BaseGUI {

    enum ParticleButton {
        NONE(-1, 0, null),
        MAGIC(9, 3, "magic"),
        ENDROD(10, 29, "endrod"),
        FIRE(11, 10, "flame"),
        CLOUD(12, 5, "cloud"),
        CRIT(13, 16, "crit"),
        HEART(18, 1, "heart"),
        MUSIC(19, 12, "note"),
        DAMAGE(20, 30, "damage"),
        TOTEM(21, 32, "totem"),
        RAINBOW(22, 27, "rainbow"),
        RESET(26, 0, ""),
        BACK(31, 0, null);

        private final int pos;
        private final int id;
        private final String ident;

        ParticleButton(int pos, int id, String ident) {
            this.pos = pos;
            this.id = id;
            this.ident = ident;
        }

        @NotNull
        public static ParticleButton forInt(int pos) {
            for (ParticleButton buttons : values()) {
                if (buttons.pos == pos) {
                    return buttons;
                }
            }
            return NONE;
        }

        public int getPos() {
            return pos;
        }

        public int getId() {
            return id;
        }

        public String getIdent() {
            return ident;
        }
    }

    public ParticleInventory(Player p) {
        super(36, ChatHelper.format("particle_inventory_title", p), p);
    }

    private ItemStack createItem(String url, ParticleButton button) {

        String clickIdent;

        if (this.p.hasPermission("trails." + button.getIdent())) {
            if (SuperTrailsAPI.getTrail(this.p) == button.getId())
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("particle_inventory_" + button.getIdent(), this.p),
                url,
                "&8&m----------------",
                ChatHelper.format("particle_inventory_" + button.getIdent() + "_desc", this.p),
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

        this.inv.setItem(ParticleButton.MAGIC.getPos(),   createItem("http://textures.minecraft.net/texture/22bdc0f2e02590cfc9101d19921b9ca12fe2345f23b274564d0b88e16727e1",   ParticleButton.MAGIC));
        this.inv.setItem(ParticleButton.ENDROD.getPos(),  createItem("http://textures.minecraft.net/texture/d80b3d019cca367ce3ca6f182fcf91ac9b8fb0be29afcedfe558ecee20b817de", ParticleButton.ENDROD));
        this.inv.setItem(ParticleButton.FIRE.getPos(),    createItem("http://textures.minecraft.net/texture/4080bbefca87dc0f36536b6508425cfc4b95ba6e8f5e6a46ff9e9cb488a9ed",   ParticleButton.FIRE));
        this.inv.setItem(ParticleButton.CLOUD.getPos(),   createItem("http://textures.minecraft.net/texture/466b10bf6ee2cd7e3ac96d9749ea616aa9c73030bdcaeffaed249e55c84994ac", ParticleButton.CLOUD));
        this.inv.setItem(ParticleButton.CRIT.getPos(),    createItem("http://textures.minecraft.net/texture/57c66f5a4b408005b336da6676e8f6a2a67eea315fb7e91360acc047802fa320", ParticleButton.CRIT));
        this.inv.setItem(ParticleButton.HEART.getPos(),   createItem("http://textures.minecraft.net/texture/f1266b748242115b303708d59ce9d5523b7d79c13f6db4ebc91dd47209eb759c", ParticleButton.HEART));
        this.inv.setItem(ParticleButton.MUSIC.getPos(),   createItem("http://textures.minecraft.net/texture/a620b82cf11e3c1371cc51eb9e312de72a6a62664494ed2cb7181b1bdfbc9278", ParticleButton.MUSIC));
        this.inv.setItem(ParticleButton.DAMAGE.getPos(),  createItem("http://textures.minecraft.net/texture/5ee118eddaee0dfb2cbc2c3d59c13a41a7d68cce945e42167aa1dcb8d0670517", ParticleButton.DAMAGE));
        this.inv.setItem(ParticleButton.TOTEM.getPos(),   createItem("http://textures.minecraft.net/texture/a1d1cf289165fbb112d8921d47708e49fb70925739b1cbd1798daff422806e8a", ParticleButton.TOTEM));
        this.inv.setItem(ParticleButton.RAINBOW.getPos(), createItem("http://textures.minecraft.net/texture/c69e3e6e5b2b92f0beb368b738b993d7ba225bf9bb2758bfc9fc2daba4a5a7d",  ParticleButton.RAINBOW));

        this.inv.setItem(ParticleButton.RESET.getPos(), ItemHelper.createItem(ChatHelper.format("particle_inventory_reset", p), Material.BARRIER));

        this.inv.setItem(ParticleButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        ParticleButton button = ParticleButton.forInt(e.getRawSlot());

        switch (button) {
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                cosmeticInventory.openInventory();
                break;
            case NONE:
                break;
            case RESET:
                SuperTrailsAPI.setTrail(0, this.p);
                this.updateItems();
                break;
            default:
                if (!p.hasPermission("trails." + button.getIdent())) break;
                SuperTrailsAPI.setTrail(button.getId(), this.p);
                this.updateItems();
                break;
        }

    }

}
