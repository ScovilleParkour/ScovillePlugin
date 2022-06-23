package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.JsonHelper;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.Music.SongItem;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RadioInventory extends BaseGUI {

    enum RadioButton {
        NONE(-1),
        BACK(36),
        STOP(39),
        RESTART(41),
        NEXT(44);

        private final int pos;

        RadioButton(int pos) {this.pos = pos;}

        @NotNull
        public static RadioButton forInt(int pos) {
            for (RadioButton buttons : values()) {
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

    int page;
    final List<SongItem> songItems = new ArrayList<>();

    public RadioInventory(Player p) {
        super(45, ChatHelper.format("radio_title", p), p);
        JsonHelper.loadItems(songItems, JsonHelper.getJsonFile("songs"));
        page = 0;
    }

    private boolean hasNextPage() {
        return songItems.size() >= 27*(page+1);
    }

    private boolean hasPrevPage() {
        return page > 0;
    }

    private List<ItemStack> getItems(int page) {

        List<ItemStack> outItems = new ArrayList<>();

        songItems.stream().filter(songItem -> Song.hasSong(songItem.getName())).forEach(songItem -> outItems.add(ItemHelper.createItem(songItem.getColor().toString() + ChatColor.BOLD + "♪ " + songItem.getName(), songItem.getItem().toItemStack())));

        if (hasPrevPage()) {
            this.inv.setItem(RadioButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_previous_page", p), Material.ARROW));
        } else {
            this.inv.setItem(RadioButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_back_to_menu", p), Material.ARROW));
        }

        this.inv.setItem(RadioButton.STOP.getPos(), ItemHelper.createItem(ChatHelper.format("radio_stop", p), "http://textures.minecraft.net/texture/ec2eb921628e4e38c9d9da39bba577da6dbfe08f10993fec8c8155aaaf976"));
        this.inv.setItem(RadioButton.RESTART.getPos(), ItemHelper.createItem(ChatHelper.format("radio_reset", p), "http://textures.minecraft.net/texture/5d9c93f8b9f2f8f91aa4377551c2738002a78816d612f39f142fc91a3d713ad"));

        if (hasNextPage()) {
            this.inv.setItem(RadioButton.NEXT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_next_page", p), Material.ARROW));
        }

        return outItems.subList(page * 27, page * 27 + Math.min(27, outItems.size() - page * 27));

    }

    @Override
    protected void initializeItems() {
        clearItems();
        createRow(3);

        List<ItemStack> items = getItems(this.page);
        for (int i = 0; i < Math.min(items.size(), 27); i++) {
            this.inv.setItem(i, items.get(i));
        }
    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);

        if (e.getRawSlot() < 27) {
            SongItem item = songItems.subList(page * 27, page * 27 + Math.min(27, songItems.size() - page * 27)).get(e.getRawSlot());
            Song song = new Song(Song.getSong(item.getName()));
            song.setIsRadio(true);
            sp.playSong(song);
        } else {
            switch (RadioButton.forInt(e.getRawSlot())) {
                case BACK:
                    if (hasPrevPage()) {
                        page--;
                        openInventory();
                    } else {
                        MainMenuInventory mmi = new MainMenuInventory(p);
                        mmi.openInventory();
                    }
                    return;
                case STOP:
                    sp.stopSong();
                    return;
                case RESTART:
                    sp.getCurrentSong().setTick(0);
                    return;
                case NEXT:
                    if (hasNextPage()) {
                        page++;
                        openInventory();
                    }
                    return;
                default:
                    return;
            }
        }

    }
}
