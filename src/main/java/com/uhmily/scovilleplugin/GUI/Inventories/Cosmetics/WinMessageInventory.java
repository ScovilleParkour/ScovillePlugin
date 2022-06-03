package com.uhmily.scovilleplugin.GUI.Inventories.Cosmetics;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.data.Nametag;
import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.GUI.Inventories.CosmeticInventory;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.WinMessage.ModifiedWinMessage;
import com.uhmily.scovilleplugin.Types.WinMessage.WinMessage;
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

public class WinMessageInventory extends BaseGUI {

    enum WinMessageButton {
        NONE(-1, null, null),
        LEET(9,"http://textures.minecraft.net/texture/2cd3f0f3b07e0fef0d866d718406dc2e635e0972e7c27c8d404beb7debd790a7", "leet"),
        CONQUISTADOR(10, "http://textures.minecraft.net/texture/ba29ea24e529dc1708ae3a2902de3e29c22a9edbbb07ecbcd27cb53602c71", "conq"),
        SPICY(11, "http://textures.minecraft.net/texture/65f7810414a2cee2bc1de12ecef7a4c89fc9b38e9d0414a90991241a5863705f", "spicy"),
        DISBELIEF(12, "http://textures.minecraft.net/texture/bc2b9b9ae622bd68adff7180f8206ec4494abbfa130e94a584ec692e8984ab2", "disbelief"),
        REVERSED(13, "http://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5", "reverse"),
        HYPE(14, "http://textures.minecraft.net/texture/e8bb2d594f4e6f00e1d33cb94f98288e4cbe1be7164f4a1bd7f725b916be602a", "hype"),
        DAILYNEWS(15, "http://textures.minecraft.net/texture/e825419e429afc040c9e68b10523b917d7b8087d63e7648b10807da8b768ee", "daily"),
        ROBOTIC(16, "http://textures.minecraft.net/texture/4dee244be08929dec08ff483bd5a856e0b6a740988174ec3047c74616f06f800", "robotic"),
        TIMESICON(29, "http://textures.minecraft.net/texture/d55fc2c1bae8e08d3e426c17c455d2ff9342286dffa3c7c23f4bd365e0c3fe", "times"),
        TIMESBUTTON(38, "", "times"),
        RAINBOWICON(33, "http://textures.minecraft.net/texture/f7b747b378a41a0a6edc86c000f040c6994a833251196c9d52c2a230f95160cc", "rainbow"),
        RAINBOWBUTTON(42, "", "rainbow"),
        RESET(17, null, ""),
        BACK(49, null, null);

        private final int pos;
        private final String url;
        private final String ident;

        WinMessageButton(int pos, String url, String ident) {
            this.pos = pos;
            this.url = url;
            this.ident = ident;
        }

        @NotNull
        public static WinMessageButton forInt(int pos) {
            for (WinMessageButton buttons : values()) {
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

    public WinMessageInventory(Player p) {
        super(54, ChatHelper.format("win_message_inventory_title", p), p);
    }

    private ItemStack createItem(WinMessageButton button) {

        String clickIdent;
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        if (this.p.hasPermission("scoville.winmessage." + button.getIdent())) {
            if (sp.getWinMessage() != null && sp.getWinMessage().getMessage().getIdent().equalsIgnoreCase(button.getIdent()))
                clickIdent = "tag_list_inventory_selected";
            else
                clickIdent = "tag_list_inventory_not_selected";
        } else
            clickIdent = "tag_list_inventory_not_unlocked";

        return ItemHelper.createItem(
                ChatHelper.format("win_message_inventory_" + button.getIdent(), this.p),
                button.getUrl(),
                "&8&m----------------",
                ChatHelper.format("win_message_inventory_" + button.getIdent() + "_desc", this.p),
                "",
                ChatHelper.format(clickIdent, this.p),
                "&8&m----------------"
        );
    }

    @Override
    protected void initializeItems() {

        clearItems();
        createRow(0);
        createRow(5);

        this.inv.setItem(WinMessageButton.LEET.getPos(), createItem(WinMessageButton.LEET));
        this.inv.setItem(WinMessageButton.CONQUISTADOR.getPos(), createItem(WinMessageButton.CONQUISTADOR));
        this.inv.setItem(WinMessageButton.SPICY.getPos(), createItem(WinMessageButton.SPICY));
        this.inv.setItem(WinMessageButton.DISBELIEF.getPos(), createItem(WinMessageButton.DISBELIEF));
        this.inv.setItem(WinMessageButton.REVERSED.getPos(), createItem(WinMessageButton.REVERSED));
        this.inv.setItem(WinMessageButton.HYPE.getPos(), createItem(WinMessageButton.HYPE));
        this.inv.setItem(WinMessageButton.DAILYNEWS.getPos(), createItem(WinMessageButton.DAILYNEWS));
        this.inv.setItem(WinMessageButton.ROBOTIC.getPos(), createItem(WinMessageButton.ROBOTIC));

        this.inv.setItem(WinMessageButton.TIMESICON.getPos(), ItemHelper.createItem(
                ChatHelper.format("win_message_inventory_" + WinMessageButton.TIMESICON.getIdent(), this.p),
                WinMessageButton.TIMESICON.getUrl(),
                "&8&m----------------",
                ChatHelper.format("win_message_inventory_" + WinMessageButton.TIMESICON.getIdent() + "_desc", this.p),
                "&8&m----------------"
        ));

        this.inv.setItem(WinMessageButton.RAINBOWICON.getPos(), ItemHelper.createItem(
                ChatHelper.format("win_message_inventory_" + WinMessageButton.RAINBOWICON.getIdent(), this.p),
                WinMessageButton.RAINBOWICON.getUrl(),
                "&8&m----------------",
                ChatHelper.format("win_message_inventory_" + WinMessageButton.RAINBOWICON.getIdent() + "_desc", this.p),
                "&8&m----------------"
        ));

        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        this.inv.setItem(WinMessageButton.TIMESBUTTON.getPos(), ItemHelper.createItem(
                ChatHelper.format(p.hasPermission("scoville.winmessage.option.times") ? (sp.getWinMessage() != null && sp.getWinMessage().isTimes() ? "options_enabled" : "options_disabled") : "win_message_not_unlocked", this.p),
                Material.INK_SACK,
                sp.getWinMessage() != null && sp.getWinMessage().isTimes() ? (byte)10 : (byte)1
        ));

        this.inv.setItem(WinMessageButton.RAINBOWBUTTON.getPos(), ItemHelper.createItem(
                ChatHelper.format(p.hasPermission("scoville.winmessage.option.rainbow") ? (sp.getWinMessage() != null && sp.getWinMessage().isRainbow() ? "options_enabled" : "options_disabled") : "win_message_not_unlocked", this.p),
                Material.INK_SACK,
                sp.getWinMessage() != null && p.hasPermission("scoville.winmessage.option.rainbow") ? (sp.getWinMessage().isRainbow() ? (byte)10 : (byte)1) : (byte)8
        ));

        this.inv.setItem(WinMessageButton.RESET.getPos(), ItemHelper.createItem(ChatHelper.format("win_message_inventory_reset", p), Material.BARRIER));

        this.inv.setItem(WinMessageButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("cosmetic_inventory_back_to_menu", p), Material.BARRIER));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        WinMessageButton button = WinMessageButton.forInt(e.getRawSlot());
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);

        switch (button) {
            case BACK:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(this.p);
                cosmeticInventory.openInventory();
                break;
            case NONE:
            case TIMESICON:
            case RAINBOWICON:
                break;
            case RESET:
                ModifiedWinMessage modifiedWinMessage = new ModifiedWinMessage();
                modifiedWinMessage.setMessage(WinMessage.DEFAULT);
                sp.setWinMessage(modifiedWinMessage);
                sp.save();
                this.updateItems();
                break;
            case RAINBOWBUTTON:
                if (!p.hasPermission("scoville.winmessage.option.rainbow")) return;
                if (sp.getWinMessage() == null) return;
                sp.getWinMessage().setRainbow(!sp.getWinMessage().isRainbow());
                sp.save();
                this.updateItems();
                break;
            case TIMESBUTTON:
                if (!p.hasPermission("scoville.winmessage.option.times")) return;
                if (sp.getWinMessage() == null) return;
                sp.getWinMessage().setTimes(!sp.getWinMessage().isTimes());
                sp.save();
                this.updateItems();
                break;
            default:
                if (!p.hasPermission("scoville.winmessage." + button.getIdent())) return;
                ModifiedWinMessage modifiedWinMessage2 = new ModifiedWinMessage();
                modifiedWinMessage2.setMessage(WinMessage.fromIdent(button.getIdent()));
                sp.setWinMessage(modifiedWinMessage2);
                sp.save();
                this.updateItems();
                break;
        }

    }

}
