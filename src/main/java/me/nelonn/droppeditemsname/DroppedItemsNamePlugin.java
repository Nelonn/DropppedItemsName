package me.nelonn.droppeditemsname;

import me.nelonn.droppeditemsname.packet.PacketSender;
import me.nelonn.droppeditemsname.packet.ProtocolLibPacketSender;
import me.nelonn.droppeditemsname.packet.ReflectionPacketSender;
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
    private PacketSender packetSender;

    @Override
    public void onEnable() {
        instance = this;

        Config.load(this);
        dataManager = new DataManager(this);
        itemUpdater = new ItemUpdater();
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            packetSender = new ProtocolLibPacketSender();
        } else {
            packetSender = new ReflectionPacketSender();
        }

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

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public static DroppedItemsNamePlugin getInstance() {
        return instance;
    }
}
