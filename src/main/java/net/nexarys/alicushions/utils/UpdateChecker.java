package net.nexarys.alicushions.utils;

import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {

    private final AliCushions plugin;
    private final int resourceId;

    public UpdateChecker(AliCushions plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    public void sendMessage(CommandSender sender, boolean updated, String version){
        if(updated || !ConfigManager.UPDATE_CHECKER) return;

        sender.sendMessage(Utils.color(String.format("&7[&d%s&7]&eA new version is available! [%s]", plugin.getDescription().getName(), version)));
        sender.sendMessage(Utils.color("&eLink: https://www.spigotmc.org/resources/holotools-new-version."+resourceId));
    }
}
