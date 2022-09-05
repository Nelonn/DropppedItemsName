package me.nelonn.droppeditemsname.item.packet;

import me.nelonn.droppeditemsname.util.NMSReflectionUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class ReflectionPacketSender implements PacketSender {
    private static Method PlayerConnection_sendPacket;
    private static Method DataWatcher_register;

    static {
        try {
            Class<?> PlayerConnection = Class.forName("net.minecraft.server.network.PlayerConnection");
            try {
                PlayerConnection_sendPacket = PlayerConnection.getMethod("sendPacket", Packet.class);
            } catch (Exception e) {
                PlayerConnection_sendPacket = PlayerConnection.getMethod("a", Packet.class);
            }
            Class<?> DataWatcher = Class.forName("net.minecraft.network.syncher.DataWatcher");
            try {
                DataWatcher_register = DataWatcher.getMethod("register", DataWatcherObject.class, Object.class);
            } catch (Exception e) {
                DataWatcher_register = DataWatcher.getMethod("a", DataWatcherObject.class, Object.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean setCustomNameVisible(@NotNull Player player, @NotNull org.bukkit.entity.Entity entity, boolean visible) {
        try {
            Entity nmsEntity = (Entity) NMSReflectionUtils.getHandle(entity);
            DataWatcher dataWatcher = new DataWatcher(nmsEntity);
            DataWatcher_register.invoke(dataWatcher, new DataWatcherObject<>(3, DataWatcherRegistry.i), visible);

            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getEntityId(), dataWatcher, true);

            PlayerConnection_sendPacket.invoke(((EntityPlayer) NMSReflectionUtils.getHandle(player)).b, packet);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
