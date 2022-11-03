package me.nelonn.droppeditemsname;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Config {
    public static long check_interval;
    public static double max_distance;
    public static double ray_size;

    private static FileConfiguration configuration;

    public static void load(@NotNull Plugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        configuration = plugin.getConfig();

        check_interval = configuration.getInt("check_interval");
        max_distance = getDouble("max_distance");
        ray_size = getDouble("ray_size");
    }

    private static double getDouble(String path) {
        return configuration.isInt(path) ? configuration.getInt(path) + 0.0 : configuration.getDouble(path);
    }
}
