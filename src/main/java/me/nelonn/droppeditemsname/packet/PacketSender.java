package me.nelonn.droppeditemsname.packet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PacketSender {
    boolean setCustomNameVisible(@NotNull Player player, @NotNull Entity entity, boolean visible);
}
