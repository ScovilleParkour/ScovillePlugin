package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import com.uhmily.scovilleplugin.Types.Difficulty;
import com.uhmily.scovilleplugin.Types.MenuItem;
import com.uhmily.scovilleplugin.Types.Rank;
import com.uhmily.scovilleplugin.Types.Season.Season;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CourseEditInventory extends BaseGUI {

    enum EditCourseButton {
        NONE(-1),
        ITEM(12),
        AUTHOR(14),
        DIFFICULTY_DEC(19),
        DIFFICULTY(22),
        DIFFICULTY_INC(25),
        SEASON_DEC(28),
        SEASON(31),
        SEASON_INC(34),
        HALLWAY(39),
        RANKUP(40),
        OPEN(41),
        CONFIRM(49);

        private final int pos;

        EditCourseButton(int pos) {this.pos = pos;}

        @NotNull
        public static EditCourseButton forInt(int pos) {
            for (EditCourseButton buttons : values()) {
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

    final Course course;
    int seasonNum, rankIndex;

    public CourseEditInventory(Player p, Course c) {
        super(54, ChatHelper.format("edit_course_title", p, c.getName()), p);
        Season sn = Season.getSeason(c);
        if (sn == null) {
            this.seasonNum = 0;
        } else {
            Season.removeCourseStatic(c);
            this.seasonNum = Season.getAvailableSeasons().indexOf(sn);
        }
        this.course = c;
        this.rankIndex = 0;
        if (course instanceof RankupCourse) {
            this.rankIndex = RankupCourse.getAvailableCourses().indexOf(((RankupCourse) course).getCurrentRank());
            ((RankupCourse) course).setCurrentRank(null);
        }
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createBorder();

        HashMap<Difficulty, ItemStack> difficultyItemStackHashMap = new HashMap<Difficulty, ItemStack>(){{
            put(Difficulty.SWEET, new ItemStack(Material.STAINED_CLAY, 1, (byte)13));
            put(Difficulty.TANGY, new ItemStack(Material.CONCRETE, 1, (byte)13));
            put(Difficulty.SAVORY, new ItemStack(Material.WOOL, 1, (byte)13));
            put(Difficulty.ZESTY, new ItemStack(Material.CONCRETE_POWDER, 1, (byte)13));
            put(Difficulty.SPICY, new ItemStack(Material.STAINED_CLAY, 1, (byte)5));
            put(Difficulty.HOT, new ItemStack(Material.CONCRETE_POWDER, 1, (byte)1));
            put(Difficulty.SIZZLING, new ItemStack(Material.CONCRETE, 1, (byte)1));
            put(Difficulty.FIERY, new ItemStack(Material.CONCRETE_POWDER, 1, (byte)14));
            put(Difficulty.SCORCHING, new ItemStack(Material.CONCRETE, 1, (byte)14));
            put(Difficulty.BLAZING, new ItemStack(Material.NETHER_WART_BLOCK));
        }};

        HashMap<Rank, ItemStack> rankItemStackHashMap = new HashMap<Rank, ItemStack>(){{
            put(Rank.BELL, new ItemStack(Material.STAINED_CLAY, 1, (byte)13));
            put(Rank.PEPPERONCINI, new ItemStack(Material.CONCRETE, 1, (byte)13));
            put(Rank.ANAHEIM, new ItemStack(Material.WOOL, 1, (byte)13));
            put(Rank.POBLANO, new ItemStack(Material.STAINED_CLAY, 1, (byte)5));
            put(Rank.GUAJILLO, new ItemStack(Material.CONCRETE, 1, (byte)5));
            put(Rank.JALAPENO, new ItemStack(Material.WOOL, 1, (byte)4));
            put(Rank.SERRANO, new ItemStack(Material.CONCRETE, 1, (byte)4));
            put(Rank.MANZANO, new ItemStack(Material.STAINED_CLAY, 1, (byte)4));
            put(Rank.CAYENNE, new ItemStack(Material.WOOL, 1, (byte)1));
            put(Rank.THAI, new ItemStack(Material.CONCRETE, 1, (byte)1));
            put(Rank.DATIL, new ItemStack(Material.STAINED_CLAY, 1, (byte)1));
            put(Rank.HABANERO, new ItemStack(Material.STAINED_CLAY, 1, (byte)14));
            put(Rank.GHOST, new ItemStack(Material.WOOL, 1, (byte)14));
            put(Rank.SCORPION, new ItemStack(Material.CONCRETE, 1, (byte)14));
            put(Rank.NAGA_VIPER, new ItemStack(Material.NETHER_WART_BLOCK));
            put(Rank.CAROLINA_REAPER, new ItemStack(Material.RED_NETHER_BRICK));
        }};

        if (this.course.getCourseItem() == null) {
            this.inv.setItem(
                    EditCourseButton.ITEM.getPos(),
                    ItemHelper.createItem(ChatHelper.format("create_gui_item", p), Material.STONE_BUTTON, ItemHelper.createLore("create_gui_item_lore", p)));
        } else {
            this.inv.setItem(EditCourseButton.ITEM.getPos(), ItemHelper.createItem(this.course.getColoredName(), this.course.getCourseItem().toItemStack()));
        }

        if (this.course.getAuthorUUID() == null) {
            this.inv.setItem(EditCourseButton.AUTHOR.getPos(), ItemHelper.createItem(ChatHelper.format("create_author", p), Material.STONE_BUTTON, ItemHelper.createLore("create_author_lore", p)));
        } else {
            this.inv.setItem(EditCourseButton.AUTHOR.getPos(), ItemHelper.createItem(UUIDFetcher.getName(this.course.getAuthorUUID()), Material.NAME_TAG));
        }

        if (!this.course.getCourseType().equals(Course.CourseType.RANKUP)) {
            Difficulty prevDiff = this.course.getDifficulty().prevDiff();
            if (prevDiff != null) {
                this.inv.setItem(EditCourseButton.DIFFICULTY_DEC.getPos(), ItemHelper.createItem(prevDiff.getColor() + "&l" + ChatHelper.format("create_difficulty", p, prevDiff.toInt()), Material.ARROW));
            }

            this.inv.setItem(EditCourseButton.DIFFICULTY.getPos(), ItemHelper.createItem(this.course.getDifficulty().getColor() + "&l" + ChatHelper.format("create_difficulty", p, this.course.getDifficulty().toInt()), difficultyItemStackHashMap.get(this.course.getDifficulty())));

            Difficulty nextDiff = this.course.getDifficulty().nextDiff();
            if (nextDiff != null) {
                this.inv.setItem(EditCourseButton.DIFFICULTY_INC.getPos(), ItemHelper.createItem(nextDiff.getColor() + "&l" + ChatHelper.format("create_difficulty", p, nextDiff.toInt()), Material.ARROW));
            }
        } else {

            ArrayList<Rank> ranks = RankupCourse.getAvailableCourses();

            if (ranks.size() > 0) {

                if (this.rankIndex != 0) {
                    Rank prevRank = ranks.get(this.rankIndex - 1);
                    this.inv.setItem(CreateCourseInventory.CreateCourseButton.DIFFICULTY_DEC.getPos(), ItemHelper.createItem(prevRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, prevRank.toString()), Material.ARROW));
                }

                Rank currRank = ranks.get(this.rankIndex);
                this.inv.setItem(CreateCourseInventory.CreateCourseButton.DIFFICULTY.getPos(), ItemHelper.createItem(currRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, currRank.toString()), rankItemStackHashMap.get(currRank)));

                if (this.rankIndex + 1 < ranks.size()) {
                    Rank nextRank = ranks.get(this.rankIndex + 1);
                    this.inv.setItem(CreateCourseInventory.CreateCourseButton.DIFFICULTY_INC.getPos(), ItemHelper.createItem(nextRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, nextRank.toString()), Material.ARROW));
                }

            }

        }

        ArrayList<Season> seasons = Season.getAvailableSeasons();

        if (this.seasonNum != 0) {
            Season prevSeason = seasons.get(this.seasonNum - 1);
            this.inv.setItem(EditCourseButton.SEASON_DEC.getPos(), ItemHelper.createItem(prevSeason.getColor() + "&l" + ChatHelper.format("create_season", p, prevSeason.getSeasonNum()), Material.ARROW));
        }

        this.inv.setItem(EditCourseButton.SEASON.getPos(), ItemHelper.createItem(seasons.get(this.seasonNum).getColor() + "&l" + ChatHelper.format("create_season", p, seasons.get(this.seasonNum).getSeasonNum()), seasons.get(this.seasonNum).getItem().toItemStack()));

        if (seasonNum + 1 < seasons.size()) {
            Season nextSeason = seasons.get(this.seasonNum + 1);
            this.inv.setItem(EditCourseButton.SEASON_INC.getPos(), ItemHelper.createItem(nextSeason.getColor() + "&l" + ChatHelper.format("create_season", p, nextSeason.getSeasonNum()), Material.ARROW));
        }

        this.inv.setItem(EditCourseButton.HALLWAY.getPos(), ItemHelper.createItem(ChatHelper.format("create_hallway", p), Material.RAILS,
                "",
                this.course.getCourseType().equals(Course.CourseType.HALLWAY) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
        ));
        this.inv.setItem(EditCourseButton.RANKUP.getPos(), ItemHelper.createItem(ChatHelper.format("create_rankup", p), Material.NAME_TAG,
                "",
                this.course.getCourseType().equals(Course.CourseType.RANKUP) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
        ));
        this.inv.setItem(EditCourseButton.OPEN.getPos(), ItemHelper.createItem(ChatHelper.format("create_open", p), Material.SAPLING,
                "",
                this.course.getCourseType().equals(Course.CourseType.OPEN) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
        ));

        this.inv.setItem(EditCourseButton.CONFIRM.getPos(), ItemHelper.createItem(ChatHelper.format("create_confirm", p), Material.ANVIL, ItemHelper.createLore("create_confirm_lore", p)));

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;
        ArrayList<Rank> ranks = RankupCourse.getAvailableCourses();
        switch (EditCourseButton.forInt(e.getRawSlot())) {
            case ITEM:
                ItemStack item = e.getCursor();
                if (isNull(item)) return;
                this.course.setColoredName(item.getItemMeta().getDisplayName());
                this.course.setCourseItem(new MenuItem(e.getCursor().clone()));
                p.setItemOnCursor(new ItemStack(Material.AIR));
                break;
            case AUTHOR:
                if (isNull(e.getCursor())) return;
                UUID authorUUID = UUIDFetcher.getUUID(e.getCursor().getItemMeta().getDisplayName());
                if (authorUUID == null) {
                    p.sendMessage(ChatHelper.format("author_not_found", p));
                }
                this.course.setAuthorUUID(authorUUID);
                p.setItemOnCursor(new ItemStack(Material.AIR));
                break;
            case DIFFICULTY_DEC:
                if (!this.course.getCourseType().equals(Course.CourseType.RANKUP)) {
                    if (this.course.getDifficulty().prevDiff() == null) return;
                    this.course.setDifficulty(this.course.getDifficulty().prevDiff());
                } else {
                    if (this.rankIndex == 0) return;
                    this.rankIndex--;
                }
                break;
            case DIFFICULTY_INC:
                if (!this.course.getCourseType().equals(Course.CourseType.RANKUP)) {
                    if (this.course.getDifficulty().nextDiff() == null) return;
                    this.course.setDifficulty(this.course.getDifficulty().nextDiff());
                } else {
                    if (this.rankIndex + 1 == ranks.size()) return;
                    this.rankIndex++;
                }
                break;
            case SEASON_DEC:
                if (this.seasonNum == 0) return;
                this.seasonNum--;
                break;
            case SEASON_INC:
                if (this.seasonNum + 1 == Season.getAvailableSeasons().size()) return;
                this.seasonNum++;
                break;
            case HALLWAY:
                this.course.setCourseType(Course.CourseType.HALLWAY);
                break;
            case RANKUP:
                this.course.setCourseType(Course.CourseType.RANKUP);
                break;
            case OPEN:
                this.course.setCourseType(Course.CourseType.OPEN);
                break;
            case CONFIRM:
                course.setStartLoc(p.getLocation());
                if (this.course.getCourseType().equals(Course.CourseType.RANKUP) && !(this.course instanceof RankupCourse)) {
                    RankupCourse rc = new RankupCourse(this.course);
                    rc.setCurrentRank(ranks.get(this.rankIndex));
                    rc.save();
                } else {
                    this.course.save();
                }
                Season.getAvailableSeasons().get(seasonNum).addCourse(course);
                p.closeInventory();
                return;
            default:
                return;
        }
        this.updateItems();
    }

}
