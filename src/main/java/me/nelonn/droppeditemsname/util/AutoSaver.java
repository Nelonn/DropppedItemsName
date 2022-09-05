package me.nelonn.droppeditemsname.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class AutoSaver implements Runnable {
    private final Plugin plugin;
    private final Saveable saveable;
    private BukkitTask task;
    private long delay;

    public AutoSaver(@NotNull Plugin plugin, @NotNull Saveable saveable, long delay) {
        this.plugin = plugin;
        this.saveable = saveable;
        this.delay = delay;
    }

    public AutoSaver(@NotNull Plugin plugin, @NotNull Saveable saveable) {
        this(plugin, saveable, 20 * 5);
    }

    public void scheduleSave() {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }
        task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay);
    }

    @Override
    public void run() {
        task = null;
        saveable.save();
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public interface Saveable {
        void save();
    }
}
