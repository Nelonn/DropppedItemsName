package me.nelonn.droppeditemsname;

import me.nelonn.marelib.util.ConfigWrapper;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Config {
    public static long check_interval;
    public static double max_distance;
    public static double ray_size;

    private static ConfigWrapper configWrapper;

    public static void load(@NotNull Plugin plugin) {
        configWrapper = new ConfigWrapper(plugin, "config.yml", true);

        check_interval = (int) configWrapper.get("check_interval");
        max_distance = getDouble("max_distance");
        ray_size = getDouble("ray_size");
    }

    private static double getDouble(String path) {
        return configWrapper.getConfig().isInt(path) ? ((int) configWrapper.get(path)) + 0.0 : (double) configWrapper.get(path);
    }
}
