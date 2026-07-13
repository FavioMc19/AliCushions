package net.nexarys.alicushions.commands;

import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Commands implements CommandExecutor, TabCompleter {
    private final AliCushions plugin = AliCushions.getInstance();

    private static final String PERM_GIVE = "alicushions.give";
    private static final String PERM_GIVE_OTHERS = "alicushions.give.others";

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {

        if (args.length == 0 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Utils.color("&#7CA1FF➤ &fUsage: &7/alicushions give &8[&7player&8] &7<&fitem&7>"));
            return true;
        }

        if (!sender.hasPermission(PERM_GIVE)) {
            sender.sendMessage(Utils.color("&#FF5C5C✖ &7You don't have permission to use this command."));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Utils.color("&#7CA1FF➤ &fUsage: &7/alicushions give &8[&7player&8] &7<&fitem&7>"));
            return true;
        }

        Map<String, NekoItem> items = plugin.getItemManager().getItems();

        Player target;
        String itemName;

        Player maybePlayer = Bukkit.getPlayer(args[1]);
        if (maybePlayer != null) {
            if (!sender.hasPermission(PERM_GIVE_OTHERS)) {
                sender.sendMessage(Utils.color("&#FF5C5C✖ &7You don't have permission to give items to other players."));
                return true;
            }

            target = maybePlayer;

            if (args.length < 3) {
                sender.sendMessage(Utils.color("&#7CA1FF➤ &fUsage: &7/alicushions give &8[&7player&8] &7<&fitem&7>"));
                return true;
            }
            itemName = args[2];
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Utils.color("&#FF5C5C✖ &7You must specify a player."));
                return true;
            }
            target = player;
            itemName = args[1];
        }

        NekoItem nekoItem = items.get(itemName.toLowerCase());
        if (nekoItem == null) {
            sender.sendMessage(Utils.color("&#FF5C5C✖ &7Item &f'" + itemName + "' &7does not exist."));
            return true;
        }

        ItemStack item = nekoItem.getItem();
        target.getInventory().addItem(item);

        if (sender == target) {
            sender.sendMessage(Utils.color("&#7CFC9A✔ &7You received &f" + itemName + "&7."));
        } else {
            sender.sendMessage(Utils.color("&#7CFC9A✔ &7Gave &f" + itemName + " &7to &f" + target.getName() + "&7."));
            target.sendMessage(Utils.color("&#7CFC9A✔ &7You received &f" + itemName + " &7from &f" + sender.getName() + "&7."));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Map<String, NekoItem> items = plugin.getItemManager().getItems();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission(PERM_GIVE)) completions.add("give");
            return filter(completions, args[0]);
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("give") || !sender.hasPermission(PERM_GIVE)) return List.of();

            if (sender.hasPermission(PERM_GIVE_OTHERS)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            completions.addAll(items.keySet());
            return filter(completions, args[1]);
        }

        if (args.length == 3) {
            if (!args[0].equalsIgnoreCase("give") || !sender.hasPermission(PERM_GIVE)) return List.of();
            if (Bukkit.getPlayer(args[1]) == null) return List.of();

            completions.addAll(items.keySet());
            return filter(completions, args[2]);
        }

        return List.of();
    }

    private List<String> filter(List<String> options, String current) {
        String lower = current.toLowerCase();
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(lower)) {
                result.add(option);
            }
        }
        return result;
    }
}