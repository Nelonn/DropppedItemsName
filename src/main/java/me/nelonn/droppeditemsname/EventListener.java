package me.nelonn.droppeditemsname;

import me.nelonn.droppeditemsname.player.PlayerTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {
    private final DroppedItemsNamePlugin plugin;

    public EventListener(DroppedItemsNamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new PlayerTask(event.getPlayer());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        plugin.getItemUpdater().updateItem(event.getEntity());
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        Item item = event.getTarget();
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (!item.isValid()) return;
            plugin.getItemUpdater().updateItem(item);
        });
    }

}
