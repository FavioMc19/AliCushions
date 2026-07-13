package net.nexarys.alicushions.managers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nexarys.alicushions.AliCushions;
import net.nexarys.alicushions.objects.Cushion;
import net.nexarys.alicushions.objects.NekoItem;
import net.nexarys.alicushions.utils.NekoConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

public class ConfigManager {
    private final AliCushions plugin = AliCushions.getInstance();

    private NekoConfig config;
    private NekoConfig cushions;
    private NekoConfig items;

    public ConfigManager() {

    }

    public void loadConfig() {
        config = new NekoConfig("config.yml", plugin);
        cushions = new NekoConfig("cushions.yml", plugin);
        items = new NekoConfig("items.yml", plugin);

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

        cushions.update();
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
}
