package net.nexarys.alicushions;

import lombok.Getter;
import net.nexarys.alicushions.commands.Commands;
import net.nexarys.alicushions.listeners.PlayerListener;
import net.nexarys.alicushions.listeners.WorldListeners;
import net.nexarys.alicushions.managers.ConfigManager;
import net.nexarys.alicushions.managers.EntityManager;
import net.nexarys.alicushions.managers.ItemManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AliCushions extends JavaPlugin {

    private EntityManager entityManager;
    private ItemManager itemManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        initClass();
        initListeners();
        initCommands();
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initClass() {
        entityManager = new EntityManager();
        itemManager = new ItemManager();
        configManager = new ConfigManager();
        configManager.loadConfig();
        entityManager.loadAllInChunks();
    }

    private void initCommands() {
        PluginCommand command = getCommand("alicushions");
        if (command != null) {
            command.setExecutor(new Commands());
        }
    }

    private void initListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new WorldListeners(), this);
    }

    public static AliCushions getInstance() {
        return JavaPlugin.getPlugin(AliCushions.class);
    }
}
