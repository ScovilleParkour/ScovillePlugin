package com.uhmily.scovilleplugin.GUI.Inventories;

import com.uhmily.scovilleplugin.GUI.BaseGUI;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.ItemHelper;
import com.uhmily.scovilleplugin.Helpers.UUIDFetcher;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import com.uhmily.scovilleplugin.Types.Season.Season;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CourseListInventory extends BaseGUI {

    enum TypeListButton {
        NONE(-1),
        HALLWAY(11),
        RANKUP(13),
        OPEN(15),
        BACK(36),
        SORT(40);

        private final int pos;

        TypeListButton(int pos) { this.pos = pos; }

        @NotNull
        public static TypeListButton forInt(int pos) {
            for (TypeListButton buttons : values()) {
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

    enum CourseListButton {
        NONE(-1),
        BACK(36),
        SORT(40),
        NEXT(44);

        private final int pos;

        CourseListButton(int pos) {this.pos = pos;}

        @NotNull
        public static CourseListButton forInt(int pos) {
            for (CourseListButton buttons : values()) {
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

    enum CourseListSortType {
        DATE,
        CREATOR,
        ALPHABET,
        DIFF,
        TYPE,
        SEASON;

        public CourseListSortType nextType() {
            return values()[(this.ordinal() + 1) % values().length];
        }

    }

    private int page;
    private CourseListSortType type = CourseListSortType.DATE;
    private int selectedSeason = -1;

    private Course.CourseType selectedType = null;

    public CourseListInventory(Player p) {
        super(45, ChatHelper.format("course_list_title", p), p);
        page = 0;
    }

    private List<Course> getCoursesForPage(int page) {
        List<Course> courses = new ArrayList<>(Course.getCourses());

        switch(type) {
            case DATE: {
                courses = courses.stream().filter(course -> !(course instanceof RankupCourse)).filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
                courses.sort(Comparator.comparing(Course::getDateCreated));
                break;
            }
            case CREATOR: {
                courses = courses.stream().filter(course -> !(course instanceof RankupCourse)).filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
                courses.sort(Comparator.comparing(course -> UUIDFetcher.getName(course.getAuthorUUID()).toLowerCase()));
                break;
            }
            case ALPHABET: {
                courses = courses.stream().filter(course -> !(course instanceof RankupCourse)).filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
                courses.sort(Comparator.comparing(Course::getName));
                break;
            }
            case DIFF: {
                courses = courses.stream().filter(course -> !(course instanceof RankupCourse)).filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
                courses.sort(Comparator.comparing(Course::getDifficulty));
                break;
            }
            case TYPE: {
                if (this.selectedType != null) {
                    courses = courses.stream().filter(course -> course.getCourseType() == this.selectedType).filter(course -> course.playerHasPermissions(this.p)).sorted(Comparator.comparing(Course::getDateCreated)).collect(Collectors.toList());
                } else {
                    return new ArrayList<>();
                }
                break;
            }
            case SEASON: {
                if (this.selectedSeason > -1) {
                    courses = Season.getSeason(this.selectedSeason).getCourses();
                    courses = courses.stream().filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
                    courses.sort(Comparator.comparing(Course::getDateCreated));
                } else {
                    return new ArrayList<>();
                }
                break;
            }
        }

        if (page * 27 >= courses.size()) {
            return new ArrayList<>();
        }

        courses = courses.stream().filter(course -> course.playerHasPermissions(this.p)).collect(Collectors.toCollection(ArrayList::new));
        if (courses.size() == 0) return new ArrayList<>();
        return courses.subList(page * 27, Math.min((page + 1) * 27, courses.size()));

    }

    private List<Season> getSeasonsForPage(int page) {
        List<Season> seasons = Season.getSeasons();
        if (page * 27 >= seasons.size()) {
            return new ArrayList<>();
        }
        seasons = seasons.stream().filter(season -> season.playerHasPermission(this.p)).collect(Collectors.toCollection(ArrayList::new));
        if (seasons.size() == 0) return new ArrayList<>();
        return seasons.subList(page * 27, Math.min((page + 1) * 27, seasons.size()));
    }

    private void initializeCourseItems() {
        List<Course> courses = getCoursesForPage(this.page);
        if (courses.size() == 0) {
            if (this.type == CourseListSortType.TYPE) {
                this.inv.setItem(TypeListButton.HALLWAY.getPos(), ItemHelper.createItem(ChatHelper.format("create_hallway", p), Material.LADDER));
                this.inv.setItem(TypeListButton.RANKUP.getPos(), ItemHelper.createItem(ChatHelper.format("create_rankup", p), Material.NAME_TAG));
                this.inv.setItem(TypeListButton.OPEN.getPos(), ItemHelper.createItem(ChatHelper.format("create_open", p), Material.SAPLING));
            } else if (this.type == CourseListSortType.SEASON) {
                List<Season> seasons = getSeasonsForPage(this.page);
                for (int i = 0; i < seasons.size(); i++) {
                    Season season = seasons.get(i);
                    inv.setItem(i, season.toItem(this.p.getUniqueId()));
                }
            }
        } else {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                inv.setItem(i, course.toItem(this.p.getUniqueId()));
            }
        }
    }

    @Override
    protected void initializeItems() {
        clearItems();
        createRow(3);
        initializeCourseItems();

        if (page == 0) {
            inv.setItem(CourseListButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_back_to_menu", p), Material.ARROW));
        } else {
            inv.setItem(CourseListButton.BACK.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_previous_page", p), Material.ARROW));
        }

        String[] sortLore = Arrays.stream(ChatHelper.formatArray("course_list_sort_lore", p)).map(s -> ChatColor.GRAY + s).toArray(String[]::new);

        switch (type) {
            case DATE: {
                sortLore[1] = ChatColor.GREEN + ChatColor.stripColor(sortLore[1]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), Material.WATCH, sortLore));
                break;
            }
            case CREATOR: {
                sortLore[2] = ChatColor.GREEN + ChatColor.stripColor(sortLore[2]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), p, sortLore));
                break;
            }
            case ALPHABET: {
                sortLore[3] = ChatColor.GREEN + ChatColor.stripColor(sortLore[3]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), Material.SKULL_ITEM, (short)3, sortLore));
                break;
            }
            case DIFF: {
                sortLore[4] = ChatColor.GREEN + ChatColor.stripColor(sortLore[4]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), Material.DIAMOND, sortLore));
                break;
            }
            case TYPE: {
                sortLore[5] = ChatColor.GREEN + ChatColor.stripColor(sortLore[5]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), Material.PAPER, sortLore));
                break;
            }
            case SEASON: {
                sortLore[6] = ChatColor.GREEN + ChatColor.stripColor(sortLore[6]);
                inv.setItem(CourseListButton.SORT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_sort_title", p), Material.BEACON, sortLore));
                break;
            }
        }

        List<Course> nextCourses = getCoursesForPage(this.page + 1);
        if (nextCourses.size() != 0) {
            inv.setItem(CourseListButton.NEXT.getPos(), ItemHelper.createItem(ChatHelper.format("course_list_next_page", p), Material.ARROW));
        }

    }

    @Override
    @EventHandler
    protected void onInventoryClick(InventoryClickEvent e) {

        if (!isThisInv(e)) return;
        if (!e.getClick().equals(ClickType.LEFT)) return;

        List<Course> courses = getCoursesForPage(this.page);
        int slot = e.getRawSlot();

        if (slot == CourseListButton.SORT.getPos()) {
            this.type = this.type.nextType();
            this.selectedSeason = -1;
            this.selectedType = null;
            this.page = 0;
            this.updateItems();
            return;
        }

        if (courses.size() == 0) {
            if (slot == TypeListButton.BACK.getPos()) {
                MainMenuInventory mmi = new MainMenuInventory(this.p);
                mmi.openInventory();
                return;
            }
            if (this.type == CourseListSortType.TYPE) {
                if (slot == TypeListButton.HALLWAY.getPos()) {
                    this.selectedType = Course.CourseType.HALLWAY;
                } else if (slot == TypeListButton.RANKUP.getPos()) {
                    this.selectedType = Course.CourseType.RANKUP;
                } else if (slot == TypeListButton.OPEN.getPos()) {
                    this.selectedType = Course.CourseType.OPEN;
                }
                this.updateItems();
            } else if (this.type == CourseListSortType.SEASON) {
                List<Season> seasons = getSeasonsForPage(this.page);
                if (slot >= seasons.size()) {
                    return;
                }
                Season season = seasons.get(slot);
                this.selectedSeason = season.getSeasonNum();
                this.updateItems();
            }
            return;
        }

        if (slot < 27) {
            if (slot >= courses.size()) {
                return;
            }
            Course course = courses.get(slot);
            course.join(this.p);
            this.p.closeInventory();
        } else if (slot == CourseListButton.BACK.getPos()) {
            if (this.page == 0) {
                MainMenuInventory mmi = new MainMenuInventory(this.p);
                this.p.closeInventory();
                mmi.openInventory();
            } else {
                this.page--;
                this.updateItems();
            }
        } else if (slot == CourseListButton.NEXT.getPos() && getCoursesForPage(this.page + 1).size() != 0) {
            this.page++;
            this.updateItems();
        }
    }
}
