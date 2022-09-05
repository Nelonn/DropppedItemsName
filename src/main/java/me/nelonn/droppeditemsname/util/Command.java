package me.nelonn.droppeditemsname.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public abstract class Command implements TabExecutor {
    private final String name;
    private final List<String> aliases;
    private final List<Command> children = new ArrayList<>();
    private String permission;
    private String permissionMessage = null;
    protected String description;
    protected String usageMessage;

    public Command(@NotNull String name) {
        this(name, null, "", "/<command>", new ArrayList<>());
    }

    public Command(@NotNull String name, String... aliases) {
        this(name, null, "", "/<command>", List.of(aliases));
    }

    public Command(@NotNull String name, String permission, String description, String usageMessage, @NotNull List<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.description = description == null ? "" : description;
        this.usageMessage = usageMessage == null ? "/<command>" : usageMessage;
        this.aliases = new ArrayList<>(aliases);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command bukkitCommand, @NotNull String command, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            if (this.permissionMessage == null) {
                sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.");
            } else if (this.permissionMessage.length() != 0) {
                sender.sendMessage(TextUtils.color(permissionMessage.replaceAll("<name>", this.name).replaceAll("<permission>", this.permission)));
            }
            return true;
        }
        if (args.length == 0) {
            onCommand(sender, command, args);
        } else {
            Command subCommand = findChild(args[0].toLowerCase(Locale.ROOT));
            if (subCommand != null) {
                return subCommand.onCommand(sender, bukkitCommand, command + " " + args[0].toLowerCase(Locale.ROOT), subArgs(args));
            } else {
                onCommand(sender, command, args);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command bukkitCommand, @NotNull String command, @NotNull String[] args) {
        if (!hasPermission(sender)) {
            return Collections.emptyList();
        }
        String commandName = args[0].toLowerCase(Locale.ROOT);
        Command subCommand = findChild(commandName);
        if (subCommand != null) {
            if (args.length > 1) {
                return subCommand.onTabComplete(sender, bukkitCommand, command + " " + commandName, subArgs(args));
            }
            return Collections.emptyList();
        }
        if (!children.isEmpty() && args.length == 1) {
            List<String> childCommands = new ArrayList<>();
            for (Command cmd : children) {
                if (!cmd.hasPermission(sender)) continue;
                predicateName(cmd, (string) -> {
                    if (string.startsWith(commandName)) {
                        childCommands.add(string);
                    }
                    return true;
                });
            }
            return childCommands;
        }
        return onTabComplete(sender, command, args);
    }

    protected abstract void onCommand(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args);

    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        return Collections.emptyList();
    }

    public boolean hasPermission(CommandSender sender) {
        return this.permission == null || this.permission.isEmpty() || sender.hasPermission(this.permission);
    }

    public void sendUsageMessage(CommandSender sender, String command) {
        TextUtils.send(sender, "&c" + usageMessage.replaceAll("<command>", command));
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addChildren(Command... children) {
        this.children.addAll(List.of(children));
    }

    public void removeChild(Command child) {
        this.children.remove(child);
    }

    public List<Command> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsageMessage() {
        return usageMessage;
    }

    public void setUsageMessage(String usageMessage) {
        this.usageMessage = usageMessage;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", aliases=" + aliases +
                ", children=" + children +
                ", permission='" + permission + '\'' +
                ", permissionMessage='" + permissionMessage + '\'' +
                ", description='" + description + '\'' +
                ", usageMessage='" + usageMessage + '\'' +
                '}';
    }

    private Command findChild(String name) {
        for (Command child : children) {
            if (predicateName(child, (string) -> string.equalsIgnoreCase(name))) return child;
        }
        return null;
    }

    private static boolean predicateName(Command command, Predicate<String> predicate) {
        if (predicate.test(command.getName())) return true;
        for (String alias : command.getAliases()) {
            if (predicate.test(alias)) return true;
        }
        return false;
    }

    private static String[] subArgs(String[] args) {
        String[] cropArgs = new String[args.length - 1];
        if (args.length == 1) return cropArgs;
        System.arraycopy(args, 1, cropArgs, 0, cropArgs.length);
        return cropArgs;
    }
}