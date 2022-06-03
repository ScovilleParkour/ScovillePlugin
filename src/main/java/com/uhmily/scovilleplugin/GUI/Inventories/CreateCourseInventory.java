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
// Emily is cute
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CreateCourseInventory extends BaseGUI {

    enum CreateCourseButton {
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

        CreateCourseButton(int pos) {this.pos = pos;}

        @NotNull
        public static CreateCourseButton forInt(int pos) {
            for (CreateCourseButton buttons : values()) {
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

    final String courseName;
    String cc;
    ItemStack guiItem;
    UUID author;
    Difficulty diff = Difficulty.SWEET;
    int rankIndex = 0;
    int seasonNum = 0;
    Course.CourseType courseType = Course.CourseType.HALLWAY;

    public CreateCourseInventory(Player p, String courseName) {
        super(54, ChatHelper.format("create_course_title", p, courseName), p);
        this.courseName = courseName;
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createBorder();

        HashMap<Difficulty, ItemStack> difficultyItemStackHashMap = new HashMap<Difficulty, ItemStack>(){{
            put(Difficulty.SWEET    , new ItemStack(Material.STAINED_CLAY     , 1, (byte)13));
            put(Difficulty.TANGY    , new ItemStack(Material.CONCRETE         , 1, (byte)13));
            put(Difficulty.SAVORY   , new ItemStack(Material.WOOL             , 1, (byte)13));
            put(Difficulty.ZESTY    , new ItemStack(Material.CONCRETE_POWDER  , 1, (byte)13));
            put(Difficulty.SPICY    , new ItemStack(Material.STAINED_CLAY     , 1, (byte)5 ));
            put(Difficulty.HOT      , new ItemStack(Material.CONCRETE_POWDER  , 1, (byte)1 ));
            put(Difficulty.SIZZLING , new ItemStack(Material.CONCRETE         , 1, (byte)1 ));
            put(Difficulty.FIERY    , new ItemStack(Material.CONCRETE_POWDER  , 1, (byte)14));
            put(Difficulty.SCORCHING, new ItemStack(Material.CONCRETE         , 1, (byte)14));
            put(Difficulty.BLAZING  , new ItemStack(Material.NETHER_WART_BLOCK             ));
        }};

        if (this.guiItem == null)
            this.inv.setItem(
                    CreateCourseButton.ITEM.getPos(),
                    ItemHelper.createItem(ChatHelper.format("create_gui_item", p), Material.STONE_BUTTON, ItemHelper.createLore("create_gui_item_lore", p))
            );
        else this.inv.setItem(CreateCourseButton.ITEM.getPos(), ItemHelper.createItem(cc, guiItem));

        if (this.author == null)
            this.inv.setItem(
                CreateCourseButton.AUTHOR.getPos(),
                ItemHelper.createItem(ChatHelper.format("create_author", p), Material.STONE_BUTTON, ItemHelper.createLore("create_author_lore", p))
            );
        else this.inv.setItem(CreateCourseButton.AUTHOR.getPos(), ItemHelper.createItem(UUIDFetcher.getName(this.author), Material.NAME_TAG));

        if (this.courseType != Course.CourseType.RANKUP) {
            Difficulty prevDiff = this.diff.prevDiff();
            if (prevDiff != null) {
                this.inv.setItem(CreateCourseButton.DIFFICULTY_DEC.getPos(), ItemHelper.createItem(prevDiff.getColor() + "&l" + ChatHelper.format("create_difficulty", p, prevDiff.toInt()), Material.ARROW));
            }

            this.inv.setItem(CreateCourseButton.DIFFICULTY.getPos(), ItemHelper.createItem(this.diff.getColor() + "&l" + ChatHelper.format("create_difficulty", p, this.diff.toInt()), difficultyItemStackHashMap.get(this.diff)));

            Difficulty nextDiff = this.diff.nextDiff();
            if (nextDiff != null) {
                this.inv.setItem(CreateCourseButton.DIFFICULTY_INC.getPos(), ItemHelper.createItem(nextDiff.getColor() + "&l" + ChatHelper.format("create_difficulty", p, nextDiff.toInt()), Material.ARROW));
            }
        } else {

            ArrayList<Rank> ranks = RankupCourse.getAvailableCourses();

            if (ranks.size() > 0) {

                if (this.rankIndex != 0) {
                    Rank prevRank = ranks.get(this.rankIndex - 1);
                    this.inv.setItem(CreateCourseButton.DIFFICULTY_DEC.getPos(), ItemHelper.createItem(prevRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, prevRank.toString()), Material.ARROW));
                }

                Rank currRank = ranks.get(this.rankIndex);
                this.inv.setItem(CreateCourseButton.DIFFICULTY.getPos(), ItemHelper.createItem(currRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, currRank.toString()), currRank.toItem()));

                if (this.rankIndex + 1 < ranks.size()) {
                    Rank nextRank = ranks.get(this.rankIndex + 1);
                    this.inv.setItem(CreateCourseButton.DIFFICULTY_INC.getPos(), ItemHelper.createItem(nextRank.getRankColor() + "&l" + ChatHelper.format("create_rank", p, nextRank.toString()), Material.ARROW));
                }

            }

        }

        ArrayList<Season> seasons = Season.getAvailableSeasons();

        if (seasonNum > 0) {
            Season prevSeason = seasons.get(this.seasonNum - 1);
            this.inv.setItem(CreateCourseButton.SEASON_DEC.getPos(), ItemHelper.createItem(prevSeason.getColor() + "&l" + ChatHelper.format("create_season", p, prevSeason.getSeasonNum()), Material.ARROW));
        }

        this.inv.setItem(CreateCourseButton.SEASON.getPos(), ItemHelper.createItem(seasons.get(this.seasonNum).getColor() + "&l" + ChatHelper.format("create_season", p, seasons.get(this.seasonNum).getSeasonNum()), seasons.get(this.seasonNum).getItem().toItemStack()));

        if (seasonNum + 1 < seasons.size()) {
            Season nextSeason = seasons.get(this.seasonNum + 1);
            this.inv.setItem(CreateCourseButton.SEASON_INC.getPos(), ItemHelper.createItem(nextSeason.getColor() + "&l" + ChatHelper.format("create_season", p, nextSeason.getSeasonNum()), Material.ARROW));
        }

        this.inv.setItem(CreateCourseButton.HALLWAY.getPos(), ItemHelper.createItem(ChatHelper.format("create_hallway", p), Material.RAILS,
                "",
                this.courseType.equals(Course.CourseType.HALLWAY) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
                ));
        this.inv.setItem(CreateCourseButton.RANKUP.getPos(), ItemHelper.createItem(ChatHelper.format("create_rankup", p), Material.NAME_TAG,
                "",
                this.courseType.equals(Course.CourseType.RANKUP) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
        ));
        this.inv.setItem(CreateCourseButton.OPEN.getPos(), ItemHelper.createItem(ChatHelper.format("create_open", p), Material.SAPLING,
                "",
                this.courseType.equals(Course.CourseType.OPEN) ? ChatHelper.format("create_selected", p) : ChatHelper.format("create_not_selected", p),
                ""
        ));

        this.inv.setItem(CreateCourseButton.CONFIRM.getPos(), ItemHelper.createItem(ChatHelper.format("create_confirm", p), Material.ANVIL, ItemHelper.createLore("create_confirm_lore", p)));

    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {
        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;
        ArrayList<Rank> ranks = RankupCourse.getAvailableCourses();
        switch (CreateCourseButton.forInt(e.getRawSlot())) {
            case ITEM:
                ItemStack item = e.getCursor();
                if (isNull(item)) return;
                this.cc = item.getItemMeta().getDisplayName();
                this.guiItem = e.getCursor().clone();
                if ((this.guiItem.getType() == Material.SKULL_ITEM || this.guiItem.getType() == Material.SKULL) && this.guiItem.getData().getData() == 3)
                    this.guiItem = ItemHelper.setURL(this.guiItem, ItemHelper.getURL(item));
                p.setItemOnCursor(new ItemStack(Material.AIR));
                break;
            case AUTHOR:
                if (isNull(e.getCursor())) return;
                UUID authorUUID = UUIDFetcher.getUUID(e.getCursor().getItemMeta().getDisplayName());
                if (authorUUID == null) {
                    p.sendMessage(ChatHelper.format("author_not_found", p));
                }
                this.author = authorUUID;
                p.setItemOnCursor(new ItemStack(Material.AIR));
                break;
            case DIFFICULTY_DEC:
                if (!this.courseType.equals(Course.CourseType.RANKUP)) {
                    if (this.diff.prevDiff() == null) return;
                    this.diff = this.diff.prevDiff();
                } else {
                    if (this.rankIndex == 0) return;
                    this.rankIndex--;
                }
                break;
            case DIFFICULTY_INC:
                if (!this.courseType.equals(Course.CourseType.RANKUP)) {
                    if (this.diff.nextDiff() == null) return;
                    this.diff = this.diff.nextDiff();
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
                this.courseType = Course.CourseType.HALLWAY;
                break;
            case RANKUP:
                this.courseType = Course.CourseType.RANKUP;
                break;
            case OPEN:
                this.courseType = Course.CourseType.OPEN;
                break;
            case CONFIRM:
                if (this.courseType != Course.CourseType.RANKUP) {
                    Course course = Course.getCourse(this.courseName);
                    course.setColoredName(ChatHelper.reverseToAltColor(this.cc, '&'));
                    course.setCourseType(this.courseType);
                    course.setCourseItem(new MenuItem(this.guiItem));
                    course.setAuthorUUID(this.author);
                    course.setDifficulty(this.diff);
                    course.setStartLoc(p.getLocation());
                    course.save();
                    Season.getAvailableSeasons().get(seasonNum).addCourse(course);
                } else {
                    RankupCourse course = RankupCourse.getCourse(this.courseName);
                    course.setCurrentRank(ranks.get(this.rankIndex));
                    course.setColoredName(ChatHelper.reverseToAltColor(this.cc, '&'));
                    course.setCourseType(this.courseType);
                    course.setCourseItem(new MenuItem(this.guiItem));
                    course.setAuthorUUID(this.author);
                    course.setDifficulty(this.diff);
                    course.setStartLoc(p.getLocation());
                    course.save();
                }
                p.closeInventory();
                return;
            default:
                return;
        }
        this.updateItems();
    }

}
