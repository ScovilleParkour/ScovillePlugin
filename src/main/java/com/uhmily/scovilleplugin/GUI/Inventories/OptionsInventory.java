package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Listeners.MusicListener;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class OptionsInventory extends BaseGUI {

    enum OptionsButton {
        NONE(-1),
        NV(19),
        CHAT(20),
        MUSIC(21),
        STARTATCP(22),
        PLAYERS(23),
        JOINING(24),
        LANG(25);

        private final int pos;

        OptionsButton(int pos) {this.pos = pos;}

        @NotNull
        public static OptionsButton forInt(int pos) {
            for (OptionsButton buttons : values()) {
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

    public OptionsInventory(Player p) {
        super(36, ChatHelper.format("options_title", p), p);
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createBorder();

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);

        this.inv.setItem(OptionsButton.NV.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_night_vision", p), Material.SPIDER_EYE));
        this.inv.setItem(OptionsButton.CHAT.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_chat", p), Material.PAPER));
        this.inv.setItem(OptionsButton.MUSIC.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_music", p), Material.RECORD_4));
        this.inv.setItem(OptionsButton.STARTATCP.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_startatcp", p), Material.SIGN));
        this.inv.setItem(OptionsButton.PLAYERS.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_players", p), p));
        this.inv.setItem(OptionsButton.JOINING.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_joining", p), Material.ENDER_PEARL));
        this.inv.setItem(OptionsButton.LANG.getPos() - 9, ItemHelper.createItem(ChatHelper.format("options_language", p), "http://textures.minecraft.net/texture/fc1e73023352cbc77b896fe7ea242b43143e013bec5bf314d41e5f26548fb2d2"));

        // Night Vision
        if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            this.inv.setItem(OptionsButton.NV.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.NV.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Toggle Chat
        if (sp.isToggleChat()) {
            this.inv.setItem(OptionsButton.CHAT.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.CHAT.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Toggle Music
        if (sp.isMusic()) {
            this.inv.setItem(OptionsButton.MUSIC.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.MUSIC.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Toggle Start At CP
        if (sp.isStartAtCP()) {
            this.inv.setItem(OptionsButton.STARTATCP.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.STARTATCP.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Toggle Players
        if (sp.isPlayerVis()) {
            this.inv.setItem(OptionsButton.PLAYERS.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.PLAYERS.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Toggle Joinplayer
        if (sp.isJoinPlayer()) {
            this.inv.setItem(OptionsButton.JOINING.getPos(), ItemHelper.createItem(ChatHelper.format("options_enabled", p), Material.INK_SACK, (short)10));
        } else {
            this.inv.setItem(OptionsButton.JOINING.getPos(), ItemHelper.createItem(ChatHelper.format("options_disabled", p), Material.INK_SACK, (short)1));
        }

        // Language Select
        this.inv.setItem(OptionsButton.LANG.getPos(), ItemHelper.createItem(ChatHelper.format("options_language_select", p), sp.getLang().getItemTexture()));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);
        if (sp == null) return;
        switch (OptionsButton.forInt(e.getRawSlot())) {
            case NV:
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                else
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1));
                break;
            case CHAT:
                sp.toggleChat();
                break;
            case MUSIC:
                sp.toggleMusic();
                if (!sp.isMusic()) sp.stopSong();
                else MusicListener.playSongAtLocation(sp, this.p.getLocation());
                break;
            case STARTATCP:
                sp.toggleStartAtCP();
                break;
            case PLAYERS:
                boolean playerVis = !sp.isPlayerVis();
                sp.setPlayerVis(playerVis);
                if (playerVis) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        this.p.showPlayer(p);
                    }
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        this.p.hidePlayer(p);
                    }
                }
                break;
            case JOINING:
                sp.toggleJoinPlayer();
                break;
            case LANG:
                sp.setLang(sp.getLang().nextLang());
                break;
        }
        sp.save();
        this.updateItems();
    }

}