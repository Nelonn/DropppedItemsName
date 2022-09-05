package me.nelonn.droppeditemsname.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSReflectionUtils {
    private static Method CraftEntity_getHandle;
    private static Method CraftPlayer_getHandle;

    public static Object getHandle(Entity entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (CraftEntity_getHandle == null) {
            CraftEntity_getHandle = entity.getClass().getMethod("getHandle");
        }
        return CraftEntity_getHandle.invoke(entity);
    }

    public static Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (CraftPlayer_getHandle == null) {
            CraftPlayer_getHandle = player.getClass().getMethod("getHandle");
        }
        return CraftPlayer_getHandle.invoke(player);
    }
}
