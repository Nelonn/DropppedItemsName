package me.nelonn.droppeditemsname.item.nbt;

import me.nelonn.droppeditemsname.util.NMSReflectionUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionNBT implements NBT {
    private static Class<?> IChatBaseComponent;
    private static Class<?> IChatBaseComponent_ChatSerializer;
    private static Method IChatBaseComponent_ChatSerializer_a;
    private static Class<?> Entity;
    private static Method Entity_setCustomName;

    static {
        try {
            IChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");
            for (Class<?> clazz : IChatBaseComponent.getDeclaredClasses()) {
                if (clazz.getSimpleName().equalsIgnoreCase("ChatSerializer")) {
                    IChatBaseComponent_ChatSerializer = clazz;
                    break;
                }
            }
            IChatBaseComponent_ChatSerializer_a = IChatBaseComponent_ChatSerializer.getMethod("a", String.class);
            Entity = Class.forName("net.minecraft.world.entity.Entity");
            try {
                Entity_setCustomName = Entity.getMethod("setCustomName", IChatBaseComponent);
            } catch (Exception e) {
                Entity_setCustomName = Entity.getMethod("b", IChatBaseComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setCustomName(@NotNull org.bukkit.entity.Entity entity, @NotNull BaseComponent name) {
        try {
            Object nmsEntity = NMSReflectionUtils.getHandle(entity);
            Entity_setCustomName.invoke(nmsEntity, IChatBaseComponent_ChatSerializer_a.invoke(null, ComponentSerializer.toString(name)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Nullable
    public String getItemDisplayName(@NotNull ItemStack itemStack) {
        String displayName = itemStack.getItemMeta().getDisplayName();
        return displayName.isEmpty() ? null : displayName;
    }

    @Override
    @Nullable
    public String getWrittenBookTitle(@NotNull ItemStack itemStack) {
        if (!(itemStack.getItemMeta() instanceof BookMeta bookMeta)) return null;
        String title = bookMeta.getTitle();
        return title != null && title.isEmpty() ? null : title;
    }
}
