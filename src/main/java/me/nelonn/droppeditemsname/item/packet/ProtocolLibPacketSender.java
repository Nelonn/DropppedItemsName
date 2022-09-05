package me.nelonn.droppeditemsname.item.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ProtocolLibPacketSender implements PacketSender {

    @Override
    public boolean setCustomNameVisible(@NotNull Player player, @NotNull Entity entity, boolean visible) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, entity.getEntityId());

            WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), visible);
            packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
