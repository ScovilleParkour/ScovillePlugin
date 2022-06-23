package com.uhmily.scovilleplugin.Types;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public enum Rank {
    BELL(ChatColor.DARK_GREEN),
    PEPPERONCINI(ChatColor.DARK_GREEN),
    ANAHEIM(ChatColor.DARK_GREEN),
    POBLANO(ChatColor.GREEN),
    GUAJILLO(ChatColor.GREEN),
    JALAPENO(ChatColor.YELLOW),
    SERRANO(ChatColor.YELLOW),
    MANZANO(ChatColor.YELLOW),
    CAYENNE(ChatColor.GOLD),
    THAI(ChatColor.GOLD),
    DATIL(ChatColor.GOLD),
    HABANERO(ChatColor.RED),
    GHOST(ChatColor.RED),
    SCORPION(ChatColor.RED),
    NAGA_VIPER(ChatColor.DARK_RED),
    CAROLINA_REAPER(ChatColor.DARK_RED),
    CAPSAICIN(ChatColor.GRAY);

    private final ChatColor rankColor;

    Rank(ChatColor color) {
        this.rankColor = color;
    }

    @NotNull
    public ChatColor getRankColor() {
        return rankColor;
    }

    public int toInt() {
        return this.ordinal();
    }

    public static Rank getRank(int i) {
        return Rank.values()[i];
    }

    public Rank nextRank() {
        if (this == Rank.CAPSAICIN) {
            return null;
        }
        return Rank.values()[toInt() + 1];
    }

    public Rank prevRank() {
        if (this == Rank.BELL) {
            return null;
        }
        return Rank.values()[this.ordinal() - 1];
    }

    public ItemStack toItem() {
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

        return rankItemStackHashMap.get(this);
    }

    @Override
    public String toString() {
        switch (this) {
            case BELL: return "Bell";
            case PEPPERONCINI: return "Pepperoncini";
            case ANAHEIM: return "Anaheim";
            case POBLANO: return "Poblano";
            case GUAJILLO: return "Guajillo";
            case JALAPENO: return "Jalapeno";
            case SERRANO: return "Serrano";
            case MANZANO: return "Manzano";
            case CAYENNE: return "Cayenne";
            case THAI: return "Thai";
            case DATIL: return "Datil";
            case HABANERO: return "Habanero";
            case GHOST: return "Ghost";
            case SCORPION: return "Scorpion";
            case NAGA_VIPER: return "Naga Viper";
            case CAROLINA_REAPER: return "Carolina Reaper";
            default: return "Capsaicin";
        }
    }

    public Group getGroup() {
        switch (this) {
            case BELL: return LuckPermsProvider.get().getGroupManager().getGroup("default");
            case PEPPERONCINI: return LuckPermsProvider.get().getGroupManager().getGroup("pepperoncini");
            case ANAHEIM: return LuckPermsProvider.get().getGroupManager().getGroup("anaheim");
            case POBLANO: return LuckPermsProvider.get().getGroupManager().getGroup("poblano");
            case GUAJILLO: return LuckPermsProvider.get().getGroupManager().getGroup("guajillo");
            case JALAPENO: return LuckPermsProvider.get().getGroupManager().getGroup("jalapeno");
            case SERRANO: return LuckPermsProvider.get().getGroupManager().getGroup("serrano");
            case MANZANO: return LuckPermsProvider.get().getGroupManager().getGroup("manzano");
            case CAYENNE: return LuckPermsProvider.get().getGroupManager().getGroup("cayenne");
            case THAI: return LuckPermsProvider.get().getGroupManager().getGroup("thai");
            case DATIL: return LuckPermsProvider.get().getGroupManager().getGroup("datil");
            case HABANERO: return LuckPermsProvider.get().getGroupManager().getGroup("habanero");
            case GHOST: return LuckPermsProvider.get().getGroupManager().getGroup("ghost");
            case SCORPION: return LuckPermsProvider.get().getGroupManager().getGroup("scorpion");
            case NAGA_VIPER: return LuckPermsProvider.get().getGroupManager().getGroup("naga_viper");
            case CAROLINA_REAPER: return LuckPermsProvider.get().getGroupManager().getGroup("carolina_reaper");
            default: return LuckPermsProvider.get().getGroupManager().getGroup("capsaicin");
        }
    }

    public void applyRank(Player p) {
        applyRank(p.getUniqueId());
    }

    public void applyRank(UUID pUUID) {
        User user = null;
        try {
            user = LuckPermsProvider.get().getUserManager().loadUser(pUUID).get();
            for (Rank rank : Rank.values()) {
                String groupPerm = "group." + rank.getGroup().getName();
                if (user.data().contains(Node.builder(groupPerm).build(), NodeEqualityPredicate.ONLY_KEY).asBoolean()) {
                    user.data().remove(Node.builder(groupPerm).build());
                }
            }
            user.data().add(Node.builder("group." + this.getGroup().getName()).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
        } catch (InterruptedException | ExecutionException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

}
