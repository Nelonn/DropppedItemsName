package me.nelonn.droppeditemsname;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemUpdater {
    public void updateItem(@NotNull Item item) {
        ItemStack itemStack = item.getItemStack();
        NBTItem nbtItem = new NBTItem(itemStack);

        BaseComponent name = null;

        NBTCompound display = nbtItem.getCompound("display");
        if (display != null && display.hasKey("Name")) {
            String string = display.getString("Name");
            if (!string.isEmpty()) {
                try {
                    name = new TextComponent(ComponentSerializer.parse(string));
                } catch (Exception ignored) {
                }
            }
        }

        if (name == null) {
            ItemMeta itemMeta = nbtItem.getItem().getItemMeta();
            if (itemMeta != null) {
                String string = itemMeta.getDisplayName();
                if (!string.isEmpty()) {
                    try {
                        name = new TextComponent(string);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (name == null && itemStack.getType() == Material.WRITTEN_BOOK) {
            String title = nbtItem.getString("title");
            if (!title.isEmpty()) {
                name = new TextComponent(title);
            }
        }

        if (name == null) {
            NamespacedKey key = itemStack.getType().getKey();
            name = new TranslatableComponent(String.format("%s.%s.%s", itemStack.getType().isBlock() ? "block" : "item", key.getNamespace(), key.getKey()));
        }

        if (itemStack.getAmount() > 1) {
            name = new TextComponent(name, new TextComponent(" x" + itemStack.getAmount()));
        }

        NBTEntity nbtEntity = new NBTEntity(item);
        nbtEntity.setString("CustomName", ComponentSerializer.toString(name));
    }
}
