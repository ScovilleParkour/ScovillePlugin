package com.uhmily.scovilleplugin.Command.Commands.HotbarCommands;

import com.uhmily.scovilleplugin.Command.ChildCommand;
import com.uhmily.scovilleplugin.Command.Commands.HotbarCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Types.Hotbar.BarItem;
import com.uhmily.scovilleplugin.Types.Hotbar.Hotbar;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class AddCommand extends ChildCommand {

    public AddCommand() {
        HotbarCommand.getInstance().registerChild("add", this);
    }

    public static boolean realCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (!p.hasPermission("scoville.hotbar.add")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(ChatHelper.format("hotbar_add_help", p));
            return false;
        }

        ItemStack CPItem = p.getInventory().getItem(1);
        ItemStack menuItem = p.getInventory().getItem(4);
        ItemStack optionsItem = p.getInventory().getItem(7);

        if (CPItem == null || CPItem.getType().equals(Material.AIR)) return false;
        if (menuItem == null || menuItem.getType().equals(Material.AIR)) return false;
        if (optionsItem == null || optionsItem.getType().equals(Material.AIR)) return false;

        String id = args[0];
        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Hotbar bar = Hotbar.getHotbar(name, id);
        bar.setCPItem(new BarItem(CPItem, p));
        bar.setMenuItem(new BarItem(menuItem, p));
        bar.setOptionsItem(new BarItem(optionsItem, p));
        bar.save();

        p.sendMessage(ChatHelper.format("hotbar_added", p, bar.getName()));

        return false;
    }

}
