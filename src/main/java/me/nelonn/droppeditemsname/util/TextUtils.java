package me.nelonn.droppeditemsname.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TextUtils {
    private TextUtils() {

    }

    private static final String SPLIT_PATTERN = "((?<=&)|(?=&))";

    public static String toJSON(String text) {
        return toJSON(new TextComponent(text));
    }

    public static String toJSON(BaseComponent... components) {
        return ComponentSerializer.toString(components);
    }

    public static void send(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(color(message));
    }

    public static String color(String string) {
        if (string == null) return null;

        String[] strings = string.split(SPLIT_PATTERN);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equalsIgnoreCase("&")) {
                i++;
                if (strings[i].charAt(0) == '#') {
                    String text = strings[i].substring(7);
                    ChatColor color = ChatColor.of(strings[i].substring(0, 7));
                    builder.append(color).append(text);
                } else {
                    String text = strings[i].substring(1);
                    ChatColor color = ChatColor.getByChar(strings[i].charAt(0));
                    if (color != null) builder.append(color);
                    builder.append(text);
                }
            } else {
                builder.append(strings[i]);
            }
        }

        return builder.toString();
    }

    public static BaseComponent[] colorComponents(@NotNull String string) {
        String[] strings = string.split(SPLIT_PATTERN);
        ComponentBuilder builder = new ComponentBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equalsIgnoreCase("&")) {
                i++;
                if (strings[i].charAt(0) == '#') {
                    String text = strings[i].substring(7);
                    ChatColor color = ChatColor.of(strings[i].substring(0, 7));

                    ComponentBuilder subComponent = new ComponentBuilder(text);
                    subComponent.color(color);

                    builder.append(subComponent.create());
                } else {
                    String text = strings[i].substring(1);
                    ChatColor color = ChatColor.getByChar(strings[i].charAt(0));

                    ComponentBuilder subComponent = new ComponentBuilder(text);
                    if (color != null) subComponent.color(color);

                    builder.append(subComponent.create());
                }
            } else {
                builder.append(strings[i]);
            }
        }

        return builder.create();
    }

    public static TextComponent colorComponent(String string) {
        return new TextComponent(colorComponents(string));
    }
}
