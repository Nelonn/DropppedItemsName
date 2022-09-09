package me.nelonn.droppeditemsname.player;

import me.nelonn.marelib.util.AutoSaver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager implements AutoSaver.Saveable {
    private final AutoSaver saver;
    private final File file;
    private final Map<UUID, Boolean> map = new HashMap<>();

    public DataManager(@NotNull Plugin plugin) {
        saver = new AutoSaver(plugin, this);
        file = new File(plugin.getDataFolder(), "data.yml");
        if (file.exists()) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(file);
            for (String uniqueIdString : data.getKeys(false)) {
                if (!data.isBoolean(uniqueIdString)) continue;
                UUID uniqueId;
                try {
                    uniqueId = UUID.fromString(uniqueIdString);
                } catch (Exception e) {
                    continue;
                }
                map.put(uniqueId, data.getBoolean(uniqueIdString));
            }
        }
    }

    public boolean getPlayerData(@NotNull UUID uniqueId) {
        return map.getOrDefault(uniqueId, true);
    }

    public boolean getPlayerData(@NotNull Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public void setPlayerData(@NotNull UUID uniqueId, boolean value) {
        Boolean previousValue = map.put(uniqueId, value);
        if (previousValue == null || previousValue != value) {
            saver.scheduleSave();
        }
    }

    public void setPlayerData(@NotNull Player player, boolean value) {
        setPlayerData(player.getUniqueId(), value);
    }

    @NotNull
    public Map<UUID, Boolean> getAllPlayersData() {
        return new HashMap<>(map);
    }

    @Override
    public void save() {
        FileConfiguration data = new YamlConfiguration();
        for (Map.Entry<UUID, Boolean> entry : map.entrySet()) {
            data.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            data.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe("[DroppedItemsName] Error while saving data:");
            e.printStackTrace();
        }
    }
}
