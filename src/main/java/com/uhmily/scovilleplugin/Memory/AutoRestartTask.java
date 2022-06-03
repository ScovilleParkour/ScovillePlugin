package com.uhmily.scovilleplugin.Memory;

import com.uhmily.scovilleplugin.Helpers.ChatHelper;
import com.uhmily.scovilleplugin.ScovillePlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AutoRestartTask implements ITask {
    private static final int DELAY = 20*60*10; //10 minutes
    private BukkitTask runnable;
    private final Runtime runtime;
    private boolean enabled;
    private boolean running;
    private long minFreeMemory;
    private int seconds = 60*10; //10 minutes
    public AutoRestartTask() {
        this.runtime = Runtime.getRuntime();
        this.running = false;
    }
    @Override
    public void loadTask() {
        if(!this.enabled) {
            return;
        }
        this.running = true;
        Bukkit.getLogger().info("Starting restart task...");
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("Checking remaining info...");
                long allocatedMemory = AutoRestartTask.this.runtime.totalMemory() - AutoRestartTask.this.runtime.freeMemory();
                long presumableFreeMemory = AutoRestartTask.this.runtime.maxMemory() - allocatedMemory;
                if(presumableFreeMemory <= AutoRestartTask.this.minFreeMemory) {
                    AutoRestartTask.this.startRestartCountdown();
                } else {
                    Bukkit.getLogger().info("Enough memory available! Checking again in 10 minutes. (alloc.: " + allocatedMemory + ", free mem.: " + presumableFreeMemory);
                }
            }
        }.runTaskTimer(ScovillePlugin.getInstance(), DELAY, DELAY);
    }
    public void startRestartCountdown() {
        if(!this.running) {
            return;
        }
        this.running = false;
        this.runnable.cancel();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ScovillePlugin.getInstance(), () -> {
            switch(this.seconds) {
                case 60*10:
                    ChatHelper.broadcastMessage("timed_restart_minutes", 10);
                    break;
                case 60*5:
                    ChatHelper.broadcastMessage("timed_restart_minutes", 5);
                    break;
                case 60*2:
                    ChatHelper.broadcastMessage("timed_restart_minutes", 2);
                    break;
                case 60:
                    ChatHelper.broadcastMessage("timed_restart_minute", 1);
                    break;
                case 30:
                case 20:
                case 15:
                case 10:
                case 9:
                case 8:
                case 7:
                case 6:
                case 5:
                case 4:
                case 3:
                case 2:
                    ChatHelper.broadcastMessage("timed_restart_seconds", this.seconds);
                    break;
                case 1:
                    ChatHelper.broadcastMessage("timed_restart_second", 1);
                    break;
                case 0:
                    ChatHelper.broadcastMessage("timed_restarting");
                    Bukkit.shutdown();
                    break;
            }
            this.seconds--;
        }, 0, 20);
    }
    public BukkitTask getRunnable() {
        return this.runnable;
    }
    public boolean isEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setMinFreeMemory(long minFreeMemory) {
        this.minFreeMemory = minFreeMemory;
    }
}
