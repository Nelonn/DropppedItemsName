package me.nelonn.droppeditemsname.player;

import me.nelonn.droppeditemsname.DroppedItemsNamePlugin;
import me.nelonn.droppeditemsname.util.Command;
import me.nelonn.droppeditemsname.util.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToggleCommand extends Command {
    private static final List<String> RUSSIAN = List.of("ru_ru", "uk_ua", "be_by", "rpr");

    public ToggleCommand() {
        super("droppeditemsname");
    }

    @Override
    protected void onCommand(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        DataManager dataManager = DroppedItemsNamePlugin.getInstance().getDataManager();
        dataManager.setPlayerData(player, !dataManager.getPlayerData(player));

        if (dataManager.getPlayerData(player)) {
            if (RUSSIAN.contains(player.getLocale())) {
                TextUtils.send(player, "Названия выброшенных предметов &aвключены");
            } else {
                TextUtils.send(player, "Dropped items name &aenabled");
            }
        } else {
            if (RUSSIAN.contains(player.getLocale())) {
                TextUtils.send(player, "Названия выброшенных предметов &cвыключены");
            } else {
                TextUtils.send(player, "Dropped items name &cdisabled");
            }
        }
    }
}
