package com.uhmily.scovilleplugin.Command.Commands.PKCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.MenuItem;
import com.uhmily.scovilleplugin.Types.Season.Season;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class SeasonCommand extends ChildCommand {

    public SeasonCommand() {
        PKCommand.getInstance().registerChild("season", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player)sender;
        if (!p.hasPermission("scoville.pk.season.command")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        String command = args[0];
        switch (command) {
            case "create": {
                if (!p.hasPermission("scoville.pk.season.create")) {
                    p.sendMessage(ChatHelper.format("no_perms", p));
                    return false;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatHelper.format("pk_season_create_help", p));
                }
                ChatColor color = ChatColor.getByChar(args[1].charAt(1));
                ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
                if (item == null || item.getType() == Material.AIR) return false;
                Optional<Season> season = Season.getSeasons().stream().max(Comparator.comparing(Season::getSeasonNum));
                Season s;
                if (season.isPresent())
                    s = Season.getSeason(season.get().getSeasonNum() + 1);
                else
                    s = Season.getSeason(1);
                s.setColor(color);
                s.setItem(new MenuItem(item));
                s.save();
                sender.sendMessage(ChatHelper.format("season_created", (Player)sender, s.getSeasonNum(), color));
                break;
            }
            case "edit": {
                if (!p.hasPermission("scoville.pk.season.edit")) return false;
                if (args.length < 3) {
                    p.sendMessage(ChatHelper.format("pk_season_edit_help", p));
                    return false;
                }
                int seasonNum = Integer.parseInt(args[1]);
                ChatColor color = ChatColor.getByChar(args[2].charAt(1));
                ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
                Season s = Season.getSeason(seasonNum);
                if (item != null && item.getType() != Material.AIR) s.setItem(new MenuItem(item));
                s.setColor(color);
                s.save();
                sender.sendMessage(ChatHelper.format("season_edited", (Player)sender, color, s.getSeasonNum()));
                break;
            }
            case "remove": {
                if (!p.hasPermission("scoville.pk.season.remove")) return false;
                break;
            }
            case "setname": {
                if (!p.hasPermission("scoville.pk.season.setname")) return false;
                if (args.length != 3) {
                    p.sendMessage(ChatHelper.format("pk_season_setname_help", p));
                    return false;
                }
                int seasonNum = Integer.parseInt(args[1]);
                String name = args[2];
                Season s = Season.getSeason(seasonNum);
                s.setName(name);
                s.save();
                break;
            }
        }

        return false;

    }

}
