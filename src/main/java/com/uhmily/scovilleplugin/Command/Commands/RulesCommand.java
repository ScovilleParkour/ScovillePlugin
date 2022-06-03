package com.uhmily.scovilleplugin.Command.Commands;

import com.uhmily.scovilleplugin.Command.BaseCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RulesCommand extends BaseCommand {

    public RulesCommand() {
        super("rules");
    }

    @Override
    public boolean safeCommand(Player p, Command paramCommand, String paramString, String[] paramArrayOfString) {

        if (!p.hasPermission("scoville.rules")) {
            p.sendMessage(ChatHelper.format("no_perms", p));
            return false;
        }
        int page;

        if (paramArrayOfString.length == 0) page = 1;
        else page = Integer.parseInt(paramArrayOfString[0]);

        TextComponent[] components = Arrays.stream(ChatHelper.formatArray("rules_page_" + page, p)).map(TextComponent::new).toArray(TextComponent[]::new);
        components[components.length - 1].setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules " + (page == 1 ? 2 : 1)));

        p.spigot().sendMessage(components);

        return false;

    }

}
