package me.nelonn.droppeditemsname.item.nbt;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NBTAPI implements NBT {
    @Override
    public boolean setCustomName(@NotNull Entity entity, @NotNull BaseComponent name) {
        NBTEntity nbtEntity = new NBTEntity(entity);
        nbtEntity.setString("CustomName", ComponentSerializer.toString(name));
        return true;
    }

    @Override
    @Nullable
    public String getItemDisplayName(@NotNull ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound display = nbtItem.getCompound("display");
        if (display == null || !display.hasKey("Name")) return null;
        return display.getString("Name");
    }

    @Override
    @Nullable
    public String getWrittenBookTitle(@NotNull ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("title")) return null;
        String title = nbtItem.getString("title");
        return title.isEmpty() ? null : title;
    }
}
