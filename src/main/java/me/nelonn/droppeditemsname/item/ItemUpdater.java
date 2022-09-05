package me.nelonn.droppeditemsname.item;

import me.nelonn.droppeditemsname.item.nbt.NBT;
import me.nelonn.droppeditemsname.item.nbt.NBTAPI;
import me.nelonn.droppeditemsname.item.nbt.ReflectionNBT;
import me.nelonn.droppeditemsname.item.packet.PacketSender;
import me.nelonn.droppeditemsname.item.packet.ProtocolLibPacketSender;
import me.nelonn.droppeditemsname.item.packet.ReflectionPacketSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUpdater implements NBT, PacketSender {
    private NBT nbt;
    private PacketSender packetSender;

    public ItemUpdater() {
        if (Bukkit.getPluginManager().isPluginEnabled("NBTAPI")) {
            nbt = new NBTAPI();
        } else {
            nbt = new ReflectionNBT();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            packetSender = new ProtocolLibPacketSender();
        } else {
            packetSender = new ReflectionPacketSender();
        }
    }

    public void updateItem(@NotNull Item item) {
        ItemStack itemStack = item.getItemStack();
        BaseComponent name = null;

        String displayName = nbt.getItemDisplayName(itemStack);
        if (displayName != null) {
            try {
                name = new TextComponent(ComponentSerializer.parse(displayName));
            } catch (Exception e) {
                name = new TextComponent(displayName);
            }
            if (name.isItalicRaw() == null) {
                name.setItalic(true);
            }
        } else if (itemStack.getType() == Material.WRITTEN_BOOK) {
            String title = nbt.getWrittenBookTitle(itemStack);
            name = new TextComponent(title);
        }

        if (name == null) {
            NamespacedKey key = itemStack.getType().getKey();
            name = new TranslatableComponent(String.format("%s.%s.%s", itemStack.getType().isBlock() ? "block" : "item", key.getNamespace(), key.getKey()));
        }

        if (itemStack.getAmount() > 1) {
            name = new TextComponent(name, new TextComponent(" x" + itemStack.getAmount()));
        }

        setCustomName(item, name);
    }

    @Override
    public boolean setCustomName(@NotNull Entity entity, @NotNull BaseComponent name) {
        return nbt.setCustomName(entity, name);
    }

    @Override
    @Nullable
    public String getItemDisplayName(@NotNull ItemStack itemStack) {
        return nbt.getItemDisplayName(itemStack);
    }

    @Override
    @Nullable
    public String getWrittenBookTitle(@NotNull ItemStack itemStack) {
        return nbt.getWrittenBookTitle(itemStack);
    }

    @Override
    public boolean setCustomNameVisible(@NotNull Player player, @NotNull Entity entity, boolean visible) {
        return packetSender.setCustomNameVisible(player, entity, visible);
    }

    public NBT getNBT() {
        return nbt;
    }

    public void setNBT(NBT nbt) {
        if (nbt instanceof ItemUpdater) return;
        this.nbt = nbt;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public void setPacketSender(PacketSender packetSender) {
        if (packetSender instanceof ItemUpdater) return;
        this.packetSender = packetSender;
    }
}
