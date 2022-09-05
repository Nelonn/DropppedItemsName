package me.nelonn.droppeditemsname.player;

import me.nelonn.droppeditemsname.Config;
import me.nelonn.droppeditemsname.DroppedItemsNamePlugin;
import me.nelonn.droppeditemsname.item.packet.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PlayerTask implements Runnable {
    private final int task;
    private final Player player;
    private Item item = null;

    public PlayerTask(Player player) {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(DroppedItemsNamePlugin.getInstance(), this, Config.check_interval, Config.check_interval);
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            stop();
            return;
        }
        if (!player.isValid()) return;
        Item newItem = null;
        if (DroppedItemsNamePlugin.getInstance().getDataManager().getPlayerData(player) && player.getGameMode() != GameMode.SPECTATOR) {
            newItem = rayTraceItem(player.getEyeLocation(), player.getEyeLocation().getDirection(), Config.max_distance, Config.ray_size);
        }
        PacketSender packetSender = DroppedItemsNamePlugin.getInstance().getItemUpdater();
        if (item != null) {
            if (item.equals(newItem)) return;
            if (item.isValid()) {
                packetSender.setCustomNameVisible(player, item, false);
            }
        }
        if (newItem != null) {
            packetSender.setCustomNameVisible(player, newItem, true);
        }
        item = newItem;
    }

    private Item rayTraceItem(Location location, Vector direction, double maxDistance, double raySize) {
        if (location.getWorld() == null) return null;
        Block block = location.getBlock();

        if (block.getType().isSolid()) {
            double bX = location.getX() - location.getBlockX();
            double bY = location.getY() - location.getBlockY();
            double bZ = location.getZ() - location.getBlockZ();
            BoundingBox boundingBox = new BoundingBox(bX, bY, bZ, bX, bY, bZ);

            for (BoundingBox blockBB : block.getCollisionShape().getBoundingBoxes()) {
                if (boundingBox.overlaps(blockBB)) return null;
            }
        }

        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, raySize, raySize, raySize);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Item itemEntity) {
                return itemEntity;
            }
        }

        if (maxDistance < raySize) return null;
        return rayTraceItem(location.clone().add(direction.clone().multiply(raySize > 0 ? raySize : 0.1)), direction, maxDistance - raySize, raySize);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(task);
    }
}
