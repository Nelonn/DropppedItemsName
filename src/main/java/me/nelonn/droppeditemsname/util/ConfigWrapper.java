package me.nelonn.droppeditemsname.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigWrapper {
    private final File file;
    private FileConfiguration config;
    private FileConfiguration defaults;

    public ConfigWrapper(@NotNull File file, FileConfiguration defaults) {
        this.file = file;
        this.defaults = defaults;
        reload();
    }

    public ConfigWrapper(@NotNull File file, @NotNull Reader reader) {
        this(file, YamlConfiguration.loadConfiguration(reader));
    }

    public ConfigWrapper(@NotNull File file) {
        this(file, (FileConfiguration) null);
    }

    public ConfigWrapper(@NotNull Plugin plugin, @NotNull String name, boolean useResourceAsDefaults) {
        file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
        if (useResourceAsDefaults) {
            InputStream is = plugin.getResource(name);
            if (is != null) {
                defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        reload();
    }

    public ConfigWrapper(@NotNull Plugin plugin, @NotNull String name) {
        this(plugin, name, false);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config = new YamlConfiguration();
        }
        if (defaults != null) {
            config.setDefaults(defaults);
        }
    }

    public File getFile() {
        return file;
    }

    @NotNull
    public FileConfiguration getConfig() {
        return config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    @Nullable
    public FileConfiguration getDefaults() {
        return defaults;
    }

    public void setDefaults(FileConfiguration defaults) {
        this.defaults = defaults;
    }

    public Object get(String path) {
        return config.get(path);
    }

    public Object get(String path, Object def) {
        return config.get(path, def);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }
}