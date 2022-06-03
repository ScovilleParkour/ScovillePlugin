package com.uhmily.scovilleplugin.Leveling;

import com.uhmily.scovilleplugin.Helpers.YMLHelper;
import com.uhmily.scovilleplugin.Types.Course.Course;
import com.uhmily.scovilleplugin.Types.Course.RankupCourse;
import com.uhmily.scovilleplugin.Types.Difficulty;
import com.uhmily.scovilleplugin.Types.Player.ScovillePlayer;
import com.uhmily.scovilleplugin.Types.Rank;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class LevelManager {

    public static int xpCalc(int level) {
        return 1000*level*level;
    }

    public static int levelCalc(long xp) {
        return (int) Math.floor(Math.sqrt(xp / 1000.0f));
    }

    public static List<String> getPermsToGrant(int level) {
        YamlConfiguration configuration = YMLHelper.getYMLConfig("level_grants");
        return configuration.getStringList(String.valueOf(level));
    }

    public static void grantPerms(Player p) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        int level = levelCalc(sp.getXp());
        for (int i = 1; i <= level; i++) {
            for (String perm : getPermsToGrant(i)) {
                User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(p);
                if (!perm.equals("fawe.permpack.basic")) {
                    user.data().add(Node.builder(perm).build());
                } else {
                    user.data().add(Node.builder(perm).context(ImmutableContextSet.of("world", "plotarea")).build());
                }
                LuckPermsProvider.get().getUserManager().saveUser(user);
            }
        }
    }

    public static void addXp(Player p, Course c) {
        ScovillePlayer sp = ScovillePlayer.getPlayer(p);
        grantXp(sp, c);
        changeLevel(sp);
        grantPerms(p);
        sp.save();
    }

    private static void changeLevel(ScovillePlayer sp) {
        Player p = Bukkit.getPlayer(sp.getUuid());
        long xp = sp.getXp();
        int level = levelCalc(xp);
        p.setLevel(level);
        float bar = (float)(xp - (long)xpCalc(level)) / (float)(xpCalc(level + 1) - xpCalc(level));
        p.setExp(bar);
    }

    private static void grantXp(ScovillePlayer sp, Course c) {
        int completions = sp.getTimesCompleted().values().stream().reduce(0, Integer::sum);
        float percentage = (float)sp.getTimesCompleted(c.getUuid())/(float)completions;
        if (completions < 20) percentage = 0.0f;
        float deficit = Math.round(10 * (1.0f - percentage)) / 10.0f;
        if (c instanceof RankupCourse) {
            sp.setXp((int)((sp.getXp() + rankToXp(((RankupCourse) c).getCurrentRank()) * deficit)));
        } else {
            sp.setXp((int)((sp.getXp() + diffToXp(c.getDifficulty()) * deficit)));
        }
    }

    private static int rankToXp(Rank rank) {
        switch (rank) {
            case BELL:
                return 150;
            case PEPPERONCINI:
                return 250;
            case ANAHEIM:
                return 375;
            case POBLANO:
                return 500;
            case GUAJILLO:
                return 750;
            case JALAPENO:
                return 1000;
            case SERRANO:
                return 2000;
            case MANZANO:
                return 3000;
            case CAYENNE:
                return 5000;
            case THAI:
                return 7500;
            case DATIL:
                return 10000;
            case HABANERO:
                return 17500;
            case GHOST:
                return 25000;
            case SCORPION:
                return 50000;
            case NAGA_VIPER:
                return 75000;
            case CAROLINA_REAPER:
                return 100000;
            default:
                return 0;
        }
    }

    private static int diffToXp(Difficulty diff) {
        switch (diff) {
            case SWEET:
                return 100;
            case TANGY:
                return 250;
            case SAVORY:
                return 500;
            case ZESTY:
                return 1000;
            case SPICY:
                return 1500;
            case HOT:
                return 3000;
            case SIZZLING:
                return 7500;
            case FIERY:
                return 12500;
            case SCORCHING:
                return 25000;
            case BLAZING:
                return 50000;
            default:
                return 0;
        }
    }

}
