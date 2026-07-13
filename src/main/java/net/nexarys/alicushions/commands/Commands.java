package net.nexarys.alicushions.commands;

import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.enums.CushionColor;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Commands implements CommandExecutor, TabCompleter {
    private final AliCushions plugin = AliCushions.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("test")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getLocation().getBlock().getLocation();

                Cushion cushion = new Cushion(UUID.randomUUID(), location, location, CushionColor.GRAY, player.getUniqueId());
                cushion.spawn();
                player.sendMessage("Cushion guardado: "+cushion.getUuid());
            }
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Utils.color("&cUso: /alicushions give [player] <item>"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Utils.color("&cUso: /alicushions give [player] <item>"));
            return true;
        }

        Map<String, NekoItem> items = plugin.getItemManager().getItems();

        Player target;
        String itemName;

        Player maybePlayer = Bukkit.getPlayer(args[1]);
        if (maybePlayer != null) {
            target = maybePlayer;

            if (args.length < 3) {
                sender.sendMessage(Utils.color("&cUso: /alicushions give [player] <item>"));
                return true;
            }
            itemName = args[2];
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Utils.color("&cDebes especificar un jugador."));
                return true;
            }
            target = player;
            itemName = args[1];
        }

        NekoItem nekoItem = items.get(itemName.toLowerCase());
        if (nekoItem == null) {
            sender.sendMessage(Utils.color("&cEl item '" + itemName + "' no existe."));
            return true;
        }

        ItemStack item = nekoItem.getItem("CushionColor", nekoItem.getCushionColor().name());
        target.getInventory().addItem(item);
        sender.sendMessage(Utils.color("&aLe diste " + itemName + " a " + target.getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Map<String, NekoItem> items = plugin.getItemManager().getItems();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
            return filter(completions, args[0]);
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("give")) return List.of();

            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            completions.addAll(items.keySet());
            return filter(completions, args[1]);
        }

        if (args.length == 3) {
            if (!args[0].equalsIgnoreCase("give")) return List.of();
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