package net.nexarys.alicushions;

import lombok.Getter;
import net.nexarys.alicushions.commands.Commands;
import net.nexarys.alicushions.listeners.PlayerListener;
import net.nexarys.alicushions.listeners.WorldListeners;
import net.nexarys.alicushions.managers.ConfigManager;
import net.nexarys.alicushions.managers.EntityManager;
import net.nexarys.alicushions.managers.ItemManager;
import net.nexarys.alicushions.managers.TextureGenerator;
import net.nexarys.alicushions.utils.Metrics;
import net.nexarys.alicushions.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AliCushions extends JavaPlugin {

    private EntityManager entityManager;
    private ItemManager itemManager;
    private ConfigManager configManager;
    private TextureGenerator textureGenerator;
    private UpdateChecker updateChecker;

    public String VERSION;
    public boolean UPDATED = true;

    @Override
    public void onEnable() {
        initLibs();
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
        textureGenerator  = new TextureGenerator();
        textureGenerator.checkAndGenerateCushions();
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

    private void initLibs() {
        new Metrics(this, 32747);

        updateChecker = new UpdateChecker(this, 137053);
        updateChecker.getVersion(version -> {
            VERSION = version;
            if(Integer.parseInt(getDescription().getVersion().replace(".", "")) < Integer.parseInt(version.replace(".", ""))) {
                UPDATED = false;
                updateChecker.sendMessage(Bukkit.getConsoleSender(), UPDATED, version);
            }
        });
    }

    public static AliCushions getInstance() {
        return JavaPlugin.getPlugin(AliCushions.class);
    }
}
