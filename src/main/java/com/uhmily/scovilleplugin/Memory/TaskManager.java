package com.uhmily.scovilleplugin.Memory;

public class TaskManager {
    private final AutoRestartTask autoRestartTask;
    private static final TaskManager taskManager = new TaskManager();
    public TaskManager() {
        this.autoRestartTask = new AutoRestartTask();
    }
    public void loadTasks() {
        this.autoRestartTask.loadTask();
        this.autoRestartTask.setMinFreeMemory(3000000000L);
    }
    public static TaskManager getInstance() {
        return taskManager;
    }
    public AutoRestartTask getAutoRestartTask() {
        return this.autoRestartTask;
    }
}