package net.nexarys.alicushions.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.NekoConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigManager {
    public static boolean UPDATE_CHECKER = true;
    private final AliCushions plugin = AliCushions.getInstance();

    private NekoConfig config;
    private NekoConfig cushions;
    private NekoConfig items;

    public ConfigManager() {

    }

    public void loadConfig() {
        config = new NekoConfig("config.yml", plugin);
        cushions = new NekoConfig("data/cushions.yml", plugin);
        items = new NekoConfig("items.yml", plugin);

        if (config.hasDefault()) {
            saveFolder("cushions", new File(plugin.getDataFolder(), "cushions"));
        }

        ConfigurationSection cushionsSection = cushions.getConfigurationSection("");
        if (cushionsSection != null) {
            for (String key : cushionsSection.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String data = cushionsSection.getString(key);
                assert data != null;
                Cushion cushion = Cushion.fromJson(JsonParser.parseString(data).getAsJsonObject());
                plugin.getEntityManager().getCushions().put(uuid, cushion);
            }
        }

        ConfigurationSection itemsSection = items.getConfigurationSection("");

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection configSection = itemsSection.getConfigurationSection(key);
            if (configSection != null) {
                NekoItem nekoItem = new NekoItem(plugin, configSection);
                nekoItem.registerRecipe();
                plugin.getItemManager().getItems().put(key, nekoItem);
            }
        }

        UPDATE_CHECKER = config.getBoolean("update_checker", true);

        cushions.update();
        config.update();
    }

    public void saveCushion(Cushion cushion) {
        JsonObject json = cushion.toJson();
        cushions.set(cushion.getUuid().toString(), json.toString());
        cushions.saveConfig();
    }

    public void removeCushion(Cushion cushion) {
        cushions.set(cushion.getUuid().toString(), null);
        cushions.saveConfig();
    }

    public void saveFolder(String folderName, File targetPath) {
        if (!targetPath.exists()) targetPath.mkdirs();

        try {
            File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!name.startsWith(folderName + "/") || entry.isDirectory()) continue;

                String fileName = name.substring(folderName.length() + 1);
                File outFile = new File(targetPath, fileName);

                try (InputStream in = plugin.getResource(name)) {
                    Files.copy(in, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            jar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
