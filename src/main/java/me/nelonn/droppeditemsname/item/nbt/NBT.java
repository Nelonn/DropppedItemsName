package me.nelonn.droppeditemsname.item.nbt;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBT {
    boolean setCustomName(@NotNull Entity entity, @NotNull BaseComponent name);

    @Nullable
    String getItemDisplayName(@NotNull ItemStack itemStack);

    @Nullable
    String getWrittenBookTitle(@NotNull ItemStack itemStack);
}
