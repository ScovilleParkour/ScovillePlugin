package com.uhmily.scovilleplugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.uhmily.scovilleplugin.Achievements.AchievementManager;
import com.uhmily.scovilleplugin.Command.Commands.*;
import com.uhmily.scovilleplugin.Command.Commands.HelpCommands.*;
import com.uhmily.scovilleplugin.Command.Commands.LBCommands.BeatenCommand;
import com.uhmily.scovilleplugin.Command.Commands.LBCommands.FastestCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommands.*;
import com.uhmily.scovilleplugin.Command.Commands.JoinPlayerCommand;
import com.uhmily.scovilleplugin.Command.Commands.PKCommands.TagCommand;
import com.uhmily.scovilleplugin.Command.Commands.PracticeCommands.CPCommand;
import com.uhmily.scovilleplugin.Command.Commands.PracticeCommands.FlyCommand;
import com.uhmily.scovilleplugin.Command.Commands.TagCommands.AddCommand;
import com.uhmily.scovilleplugin.Command.Commands.TagCommands.RemoveCommand;
import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.Helpers.JsonHelper;
import com.uhmily.scovilleplugin.Helpers.SaveHelper;
import com.uhmily.scovilleplugin.Listeners.*;
import com.uhmily.scovilleplugin.Memory.TaskManager;
import com.uhmily.scovilleplugin.Types.Music.Song;
import com.uhmily.scovilleplugin.Types.ScoPlaceholder;
import com.uhmily.scovilleplugin.Types.Tag.TagManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScovillePlugin extends JavaPlugin {

    public static final String PERMISSION_NAME = "scovilleplugin";
    private static ScovillePlugin instance;
    public static StringFlag SONG_FLAG;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.saveResource("strings-en.yml", true);
        this.saveResource("strings-de.yml", true);
        this.saveResource("strings-es.yml", true);
        this.saveResource("strings-jp.yml", true);
        this.saveResource("level_grants.yml", true);
        this.saveResource("emotes.yml", true);

        new PKCommand();
        new MainMenuCommand();
        new LoadCommand();
        new SaveCommand();
        new CreateCommand();
        new EditCommand();
        new PlateCommand();
        new TPSignCommand();
        new OptionsCommand();
        new SeasonCommand();
        new GetHotbarCommand();
        new TagCommand();
        new PromoteCommand();
        new DemoteCommand();
        new RestartCommand();
        new XPCommand();

        new LeaderboardCommand();
        new FastestCommand();
        new BeatenCommand();
        new com.uhmily.scovilleplugin.Command.Commands.LBCommands.RemoveCommand();

        new LobbyCommand();
        new ListDataCommand();
        new JoinCommand();
        new SetAltJoinCommand();
        new PlayingCommand();
        new JoinPlayerCommand();
        new RadioCommand();
        new MyRankCommand();
        new NVCommand();
        new RulesCommand();
        new TutorialCommand();
        new HubCommand();

        new HelpCommand();
        new StaffCommand();
        new InfoCommand();
        new ListCommand();
        new SocialsCommand();
        new CommandsCommand();

        new PracticeCommand();
        new CPCommand();
        new FlyCommand();

        new com.uhmily.scovilleplugin.Command.Commands.TagCommand();
        new AddCommand();
        new RemoveCommand();

        new HotbarCommand();
        new com.uhmily.scovilleplugin.Command.Commands.HotbarCommands.AddCommand();
        new com.uhmily.scovilleplugin.Command.Commands.HotbarCommands.RemoveCommand();

        new ScovillePlayerListener();
        new StartTimeListener();
        new EndPlateListener();
        new TPSignListener();
        new CheckpointListener();
        new ItemListener();
        new CheckpointItemListener();
        new MenuItemListener();
        new OptionsItemListener();
        new ToggleChatListener();
        new TogglePlayerListener();
        new DisableCollisionListener();
        new MusicListener();
        new RedstoneLampListener();
        new DamageListener();
        new TagJoinListener();
        new ChatColorListener();
        new JoinLeaveMessageListener();
        new FoodListener();
        new TagSignListener();
        new CreativeItemListener();

        new ScoPlaceholder().register();

        JsonHelper.loadData();
        Song.loadSongs();
        TagManager.load();

        AchievementManager.getInstance().register();
        TaskManager.getInstance().getAutoRestartTask().setEnabled(true);
        TaskManager.getInstance().loadTasks();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            JsonHelper.saveData();
            TagManager.save();
            SaveHelper.makeBackup();
            ChatHelper.broadcastMessage("saved_data");
        }, 12000L, 12000L);

    }

    @Override
    public void onLoad() {

        FlagRegistry registry = WGBukkit.getPlugin().getFlagRegistry();
        try {
            StringFlag flag = new StringFlag("song", (String)null);
            registry.register(flag);
            SONG_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("song");
            if (existing instanceof StringFlag) {
                SONG_FLAG = (StringFlag) existing;
            }
        }

    }

    @Override
    public void onDisable() {

        while (true) {
            try {
                JsonHelper.saveData();
                SaveHelper.makeBackup();
                TagManager.save();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static ScovillePlugin getInstance() {
        return instance;
    }
}
