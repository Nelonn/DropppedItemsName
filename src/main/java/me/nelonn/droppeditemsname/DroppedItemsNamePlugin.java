package me.nelonn.droppeditemsname;

import me.nelonn.droppeditemsname.item.ItemUpdater;
import me.nelonn.droppeditemsname.player.DataManager;
import me.nelonn.droppeditemsname.player.PlayerTask;
import me.nelonn.droppeditemsname.player.ToggleCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DroppedItemsNamePlugin extends JavaPlugin {
    private static DroppedItemsNamePlugin instance;

    private DataManager dataManager;
    private ItemUpdater itemUpdater;

    @Override
    public void onEnable() {
        instance = this;

        Config.load(this);
        dataManager = new DataManager(this);
        itemUpdater = new ItemUpdater();

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
        Bukkit.getOnlinePlayers().forEach(PlayerTask::new);

        getCommand("droppeditemsname").setExecutor(new ToggleCommand());
    }

    @Override
    public void onDisable() {
        dataManager.save();
        dataManager = null;
        itemUpdater = null;
        HandlerList.unregisterAll(this);
        instance = null;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ItemUpdater getItemUpdater() {
        return itemUpdater;
    }

    public static DroppedItemsNamePlugin getInstance() {
        return instance;
    }
}
