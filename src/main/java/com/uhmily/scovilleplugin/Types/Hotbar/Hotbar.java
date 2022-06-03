package com.uhmily.scovilleplugin.Types.Hotbar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uhmily.scovilleplugin.Types.ScovilleObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class Hotbar extends ScovilleObject {

    private static final ArrayList<Hotbar> hotbars = new ArrayList<>();

    private String name = "", id = "";
    private BarItem CPItem = null, menuItem = null, optionsItem = null;

    private Hotbar(String name, String id) {
        this.name = name;
        this.id = id;
        this.CPItem = new BarItem(new ItemStack(Material.STONE));
        this.menuItem = new BarItem(new ItemStack(Material.STONE));
        this.optionsItem = new BarItem(new ItemStack(Material.STONE));
    }

    public static ArrayList<Hotbar> getHotbars() {
        return hotbars;
    }

    @JsonCreator
    public static Hotbar getHotbar(@JsonProperty("name") String name, @JsonProperty("id") String id) {
        Optional<Hotbar> s = hotbars.stream().filter(bar -> bar.getId().equalsIgnoreCase(id)).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return new Hotbar(name, id);
    }

    @Nullable
    public static Hotbar getHotbar(UUID uuid) {
        Optional<Hotbar> s = hotbars.stream().filter(bar -> bar.getUuid().equals(uuid)).findFirst();
        if (s.isPresent()) {
            return s.get();
        }
        return null;
    }

    public void save() {
        hotbars.removeIf(bar -> bar.getUuid().equals(this.getUuid()));
        hotbars.add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BarItem getCPItem() {
        return CPItem;
    }

    public void setCPItem(BarItem CPItem) {
        this.CPItem = CPItem;
    }

    public BarItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(BarItem menuItem) {
        this.menuItem = menuItem;
    }

    public BarItem getOptionsItem() {
        return optionsItem;
    }

    public void setOptionsItem(BarItem optionsItem) {
        this.optionsItem = optionsItem;
    }
}
