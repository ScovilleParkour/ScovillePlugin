package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import me.clip.deluxetags.DeluxeTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class MainMenuInventory extends BaseGUI {

    enum MainMenuButton {
        NONE(-1),
        GREENHOUSE(10),
        COURSES(12),
        CREATIVE(14),
        TAGS(16),
        SPAWN(36),
        RADIO(40),
        LEVELS(44);

        private final int pos;

        MainMenuButton(int pos) {this.pos = pos;}

        @NotNull
        public static MainMenuButton forInt(int pos) {
            for (MainMenuButton buttons : values()) {
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

    public MainMenuInventory(Player p) {
        super(45, ChatHelper.format("main_menu_title", p), p);
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createRow(3);
        this.inv.setItem(MainMenuButton.GREENHOUSE.getPos()  , ItemHelper.createItem(ChatHelper.format("main_menu_greenhouse_button_title", p), Material.LEAVES       , ItemHelper.createLore("main_menu_greenhouse_button_lore", p)));
        this.inv.setItem(MainMenuButton.COURSES.getPos()     , ItemHelper.createItem(ChatHelper.format("main_menu_courses_button_title", p), Material.EYE_OF_ENDER    , ItemHelper.createLore("main_menu_courses_button_lore", p)));
        this.inv.setItem(MainMenuButton.CREATIVE.getPos()    , ItemHelper.createItem(ChatHelper.format("main_menu_creative_button_title", p), Material.DIAMOND_PICKAXE, ItemHelper.createLore("main_menu_creative_button_lore", p)));
        this.inv.setItem(MainMenuButton.TAGS.getPos()        , ItemHelper.createItem(ChatHelper.format("main_menu_cosmetics_button_title", p), Material.NAME_TAG      , ItemHelper.createLore("main_menu_cosmetics_button_lore", p)));
        this.inv.setItem(MainMenuButton.SPAWN.getPos()       , ItemHelper.createItem(ChatHelper.format("main_menu_spawn_button_title", p), Material.BEACON            , ItemHelper.createLore("main_menu_spawn_button_lore", p)));
        this.inv.setItem(MainMenuButton.RADIO.getPos(), ItemHelper.createItem(ChatHelper.format("main_menu_radio_button_title", p), Material.RECORD_4    , ItemHelper.createLore("main_menu_radio_button_lore", p)));
        this.inv.setItem(MainMenuButton.LEVELS.getPos()        , ItemHelper.createItem(ChatHelper.format("main_menu_level_button_title", p), Material.EXP_BOTTLE        , ItemHelper.createLore("main_menu_level_button_lore", p)));
    }

    private static HashMap<UUID, Integer> tagStep = new HashMap<>();

    private void handleTagClick(int slot) {

        final int[] clicks = new int[]{ 9, 3, 2, 8, 7, 6, 5, 4, 1 };
        final String QUESTION_TAG = "deluxetags.tag.???";

        UUID pUUID = p.getUniqueId();
        if (!tagStep.containsKey(pUUID)) tagStep.put(pUUID, 0);

        if (p.hasPermission(QUESTION_TAG) && p.isPermissionSet(QUESTION_TAG))
            return;

        if (clicks[tagStep.get(pUUID)] == slot - 26) {
            tagStep.put(pUUID, tagStep.get(pUUID) + 1);
        } else {
            tagStep.put(pUUID, 0);
        }

        if (tagStep.get(pUUID) == 9) {
            User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
            user.data().add(Node.builder(QUESTION_TAG).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
            ChatHelper.broadcastMessage("tag_unlocked", p.getName(), DeluxeTag.getLoadedTag("???").getDisplayTag());
        }

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;
        ScovillePlayer sp = ScovillePlayer.getPlayer(this.p);
        switch (MainMenuButton.forInt(e.getRawSlot())) {
            case GREENHOUSE:
                RankupCourse rankupCourse = RankupCourse.getCourseOrNull(sp.getRank());
                if (rankupCourse == null) {
                    p.sendMessage(ChatHelper.format("course_not_found", p));
                    return;
                }
                rankupCourse.join(p);
                break;
            case COURSES:
                CourseListInventory cli = new CourseListInventory(this.p);
                cli.openInventory();
                break;
            case CREATIVE:
                p.teleport(new Location(Bukkit.getWorld("PlotArea"), 0.5, 65, 0.5, 90, 0));
                sp.setCurrentlyPlaying(null);
                sp.save();
                break;
            case TAGS:
                CosmeticInventory cosmeticInventory = new CosmeticInventory(p);
                cosmeticInventory.openInventory();
                break;
            case SPAWN:
                p.performCommand("hub");
                break;
            case RADIO:
                RadioInventory radioInventory = new RadioInventory(p);
                radioInventory.openInventory();
                break;
            case LEVELS:
                // TODO: Implement Leveling Button
                break;
            default:
                break;
        }

        if (e.getRawSlot() > 26 && e.getRawSlot() < 36)
            handleTagClick(e.getRawSlot());

    }

}
