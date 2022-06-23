package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Leveling.LevelManager;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class LevelingInventory extends BaseGUI {

    private enum LevelingButton {
        NONE(-1),
        BACK(36),
        PROGRESS(39),
        JUMP(41),
        NEXT(44);

        private final int pos;

        LevelingButton(int pos) {
            this.pos = pos;
        }

        @NotNull
        public static LevelingButton forInt(int pos) {
            for (LevelingButton buttons : values()) {
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

    private static final int MAX_LEVEL = 100;

    private int page;

    public LevelingInventory(Player p) {
        super(45, ChatHelper.format("leveling_inventory_title", p), p);
    }

    private boolean hasPrevPage() {
        return page > 0;
    }

    private boolean hasNextPage() {
        return page + 1 < MAX_LEVEL / 20;
    }

    private String getLevelingProgress(float progress) {
        final String BARS = "|";
        final int BAR_COUNT = 10;

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return "";
        int progressNum = (int) Math.floor(BAR_COUNT * progress);
        return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + StringUtils.repeat(BARS, progressNum) + ChatColor.GRAY + StringUtils.repeat(BARS, BAR_COUNT - progressNum) + ChatColor.DARK_GRAY + "]";
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createRow(0);
        createRow(3);

        for (int i = 2 + page * 20; i <= (page + 1) * 20; i += 2) {
            final int pos;
            if (i % 20 == 0) {
                pos = 22;
            } else {
                pos = 8 + (i - (page * 20)) / 2;
            }
            this.inv.setItem(pos, ItemHelper.createItem(ChatHelper.format("leveling_inventory_level_title", p, i), Material.EXP_BOTTLE, ChatHelper.formatArray("level_" + i + "_desc", p)));
        }

        if (hasPrevPage()) {
            this.inv.setItem(LevelingButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("leveling_inventory_prev_button_title", p), Material.ARROW));
        } else {
            this.inv.setItem(LevelingButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("leveling_inventory_back_button_title", p), Material.ARROW));
        }

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;
        int level = LevelManager.levelCalc(sp.getXp());
        float progress = (sp.getXp() - LevelManager.xpCalc(level)) / (float)(LevelManager.xpCalc(level + 1) - LevelManager.xpCalc(level));
        long xpNeeded = LevelManager.xpCalc(level + 1) - sp.getXp();
        this.inv.setItem(LevelingButton.PROGRESS.getPos(), ItemHelper.createItem(ChatHelper.format("leveling_inventory_progress_button_title", p), Material.EMERALD, ChatHelper.formatArray("leveling_inventory_progress_button_lore", p, level, getLevelingProgress(progress), Math.round(progress * 100), xpNeeded)));

        if (hasNextPage()) {
            this.inv.setItem(LevelingButton.NEXT.getPos(), ItemHelper.createItem(ChatHelper.format("leveling_inventory_next_button_title", p), Material.ARROW));
        }

        this.inv.setItem(LevelingButton.JUMP.getPos(), ItemHelper.createItem(ChatHelper.format("leveling_inventory_jump_button_title", p), Material.COMPASS, ChatHelper.formatArray("leveling_inventory_jump_button_lore", p)));

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        if (sp == null) return;

        switch (LevelingButton.forInt(e.getRawSlot())) {
            case BACK: {
                if (hasPrevPage()) {
                    page--;
                    initializeItems();
                } else {
                    MainMenuInventory mainMenuInventory = new MainMenuInventory(p);
                    mainMenuInventory.openInventory();
                }
                break;
            }
            case JUMP: {
                this.page = (int)Math.floor((LevelManager.levelCalc(sp.getXp()) / 20.0f));
                initializeItems();
                break;
            }
            case NEXT: {
                if (hasNextPage()) {
                    page++;
                    initializeItems();
                }
                break;
            }
            default:
                break;
        }
    }

}
